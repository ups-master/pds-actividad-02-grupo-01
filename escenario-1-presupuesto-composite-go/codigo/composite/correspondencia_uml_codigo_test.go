package composite

import (
	"go/ast"
	"go/parser"
	"go/printer"
	"go/token"
	"os"
	"path/filepath"
	"regexp"
	"slices"
	"strings"
	"testing"
)

// El UML es contrato: esta prueba lee el diagrama y comprueba que cada tipo,
// campo y metodo que aparece en el coincide con el codigo, incluida su firma.

const rutaUML = "../../uml/compositeE1_go.puml"

var (
	reTipo    = regexp.MustCompile(`^(?:interface|struct)\s+(\w+)`)
	reMetodo  = regexp.MustCompile(`^[+-]\s*(?:\{static\}\s*)?(\w+\(.*)$`)
	reCampo   = regexp.MustCompile(`^[+-]\s*(\w+)\s+(.+)$`)
	reRealiza = regexp.MustCompile(`^(\w+)\s+\.\.\|>\s+(\w+)`)
)

// miembrosUML devuelve, por tipo, sus miembros como "Nombre(params) resultado"
// o "nombre tipo"; y las relaciones de realizacion como "Tipo ..|> Interfaz".
func miembrosUML(t *testing.T) (map[string][]string, []string) {
	contenido, err := os.ReadFile(rutaUML)
	if err != nil {
		t.Fatal(err)
	}
	tipos := map[string][]string{}
	var realizaciones []string
	actual := ""
	for _, linea := range strings.Split(string(contenido), "\n") {
		linea = strings.TrimSpace(linea)
		if m := reTipo.FindStringSubmatch(linea); m != nil {
			actual = m[1]
			tipos[actual] = nil
		} else if linea == "}" {
			actual = ""
		} else if m := reMetodo.FindStringSubmatch(linea); actual != "" && m != nil {
			tipos[actual] = append(tipos[actual], m[1])
		} else if m := reCampo.FindStringSubmatch(linea); actual != "" && m != nil {
			tipos[actual] = append(tipos[actual], m[1]+" "+m[2])
		} else if m := reRealiza.FindStringSubmatch(linea); m != nil {
			realizaciones = append(realizaciones, m[1]+" ..|> "+m[2])
		}
	}
	return tipos, realizaciones
}

// miembrosGo devuelve lo mismo que miembrosUML, pero leido del codigo fuente.
func miembrosGo(t *testing.T) map[string][]string {
	rutas, _ := filepath.Glob("*.go")
	fset := token.NewFileSet()
	texto := func(n any) string {
		var b strings.Builder
		printer.Fprint(&b, fset, n)
		return b.String()
	}
	// firma convierte "func(codigo, descripcion string) T" en "(codigo string, descripcion string) T".
	firma := func(f *ast.FuncType) string {
		var params []string
		for _, p := range f.Params.List {
			for _, n := range p.Names {
				params = append(params, n.Name+" "+texto(p.Type))
			}
		}
		s := "(" + strings.Join(params, ", ") + ")"
		if f.Results != nil {
			s += " " + texto(f.Results.List[0].Type)
		}
		return s
	}

	tipos := map[string][]string{}
	for _, ruta := range rutas {
		if strings.HasSuffix(ruta, "_test.go") {
			continue
		}
		archivo, err := parser.ParseFile(fset, ruta, nil, 0)
		if err != nil {
			t.Fatal(err)
		}
		ast.Inspect(archivo, func(n ast.Node) bool {
			switch d := n.(type) {
			case *ast.TypeSpec:
				if _, ok := tipos[d.Name.Name]; !ok {
					tipos[d.Name.Name] = nil
				}
				switch tt := d.Type.(type) {
				case *ast.InterfaceType:
					for _, m := range tt.Methods.List {
						tipos[d.Name.Name] = append(tipos[d.Name.Name], m.Names[0].Name+firma(m.Type.(*ast.FuncType)))
					}
				case *ast.StructType:
					for _, c := range tt.Fields.List {
						if len(c.Names) == 0 {
							tipos[d.Name.Name] = append(tipos[d.Name.Name], texto(c.Type)+" <<embebido>>")
						}
						for _, nombre := range c.Names {
							tipos[d.Name.Name] = append(tipos[d.Name.Name], nombre.Name+" "+texto(c.Type))
						}
					}
				}
			case *ast.FuncDecl:
				// Los constructores NuevoX se asignan al tipo que devuelven ({static} en el UML).
				var tipo string
				if d.Recv != nil {
					tipo = strings.TrimPrefix(texto(d.Recv.List[0].Type), "*")
				} else {
					tipo = strings.TrimPrefix(texto(d.Type.Results.List[0].Type), "*")
				}
				tipos[tipo] = append(tipos[tipo], d.Name.Name+firma(d.Type))
			}
			return true
		})
	}
	return tipos
}

func TestCorrespondenciaUmlCodigo(t *testing.T) {
	uml, realizaciones := miembrosUML(t)
	codigo := miembrosGo(t)

	for tipo := range codigo {
		if _, ok := uml[tipo]; !ok {
			t.Errorf("el tipo %s existe en Go pero no en el UML", tipo)
		}
	}
	for tipo, miembros := range uml {
		enGo, ok := codigo[tipo]
		if !ok {
			t.Errorf("el tipo %s existe en el UML pero no en Go", tipo)
			continue
		}
		slices.Sort(miembros)
		slices.Sort(enGo)
		if !slices.Equal(miembros, enGo) {
			t.Errorf("%s no coincide\n  UML: %q\n  Go:  %q", tipo, miembros, enGo)
		}
	}

	// Las realizaciones del UML deben cumplirse en el codigo.
	esperadas := []string{
		"GrupoPresupuestario ..|> ElementoPresupuestario",
		"PartidaPresupuestaria ..|> ElementoPresupuestario",
	}
	slices.Sort(realizaciones)
	if !slices.Equal(realizaciones, esperadas) {
		t.Errorf("realizaciones del UML %q, se esperaba %q", realizaciones, esperadas)
	}
	var _ ElementoPresupuestario = (*PartidaPresupuestaria)(nil)
	var _ ElementoPresupuestario = (*GrupoPresupuestario)(nil)
}
