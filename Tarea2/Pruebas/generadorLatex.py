import pruebasRandom
from datetime import datetime
from pylatex import Document, Section, Subsection, Tabular, Math, Command
from pylatex.utils import italic, NoEscape

def mostrarPromedio(promedio, doc):
    with doc.create(Subsection('Promedio')):
        doc.append( Math(data=['promedio','=', (float)(promedio)]) )

def mostrarVarianza(varianza, doc):
    with doc.create(Subsection('Varianza')):
        doc.append( Math(data=['varianza','=',varianza[0]]) )
        doc.append( Math(data=['desviacion_estandar','=',varianza[1]]) )

def mostrarCorridas(corridas, doc):
    with doc.create(Subsection('Prueba de Corridas')):
        doc.append( Math(data=['corridas','=',corridas[0]]) )
        doc.append( Math(data=['E(h)','=',corridas[1]]) )
        doc.append( Math(data=['S(h)2','=',corridas[2]]) )
        doc.append( Math(data=['S(h)','=',corridas[3]]) )
        doc.append( Math(data=['Z(0)','=',corridas[4]]) )
        
def mostrarDigitos(digitos, doc):
    with doc.create(Subsection('Prueba de Huecos Digitos')):
        jiCuadrado = 0.0
        total = pruebasRandom.sumarTablas(digitos,20,10,True)
        sumaTemporal = 0
        frecuenciaTemporal = 0.0
        
        tablaDigitos = Tabular('|c|c|c|c|c|c|c|')
        tablaDigitos.add_row(('Huecos','Pe','Fo','Fe','(Fo-Fe)','(Fo-Fe)2','(Fo-Fe)2/Fe'))
        for i in range(21):
            if(i != 20):
                promedio = 0.1*(0.9)**i
                frecuenciaObs = digitos[i][10]
                frecuenciaTemporal = frecuenciaObs
                sumaTemporal += frecuenciaObs
                
            else:
                promedio = (0.9)**i
                sumaTemporal += digitos[i][10]
                frecuenciaObs = digitos[i][10]
                frecuenciaTemporal = total - sumaTemporal
                frecuenciaTemporal += digitos[i][10]

            frecuenciaEsperada = promedio*total
            fObs_fEsp = frecuenciaTemporal-frecuenciaEsperada
            fObs_fEsp2 = fObs_fEsp**2
            formula = fObs_fEsp2/frecuenciaEsperada

            fila = (i,promedio,frecuenciaObs,frecuenciaEsperada,fObs_fEsp,fObs_fEsp2,formula)

            tablaDigitos.add_hline()
            tablaDigitos.add_row(fila)

            jiCuadrado += formula
        doc.append(tablaDigitos)
        doc.append( Math(data=['Total','=',total]) )        
        doc.append( Math(data=['X2(0)','=',jiCuadrado]) )

def mostrarNumeros(archivo,doc):
    with doc.create(Subsection("Prueba con Numeros")):
        intervalo = [[0.0,0.1], [0.1,0.25],[0.25,0.45],[0.45,0.7],[0.7, 1]]
        numeros = pruebasRandom.obtenerRandom(archivo,intervalo)
        jiCuadrado = 0.0

        for i in range(5):
            with doc.create(Tabular('|c|c|c|c|c|c|c|')) as table:
                table.add_row(('Huecos','Pe','Fo','Fe','(Fo-Fe)','(Fo-Fe)2','(Fo-Fe)2/Fe'))
                
                t = intervalo[i][1] - intervalo[i][0]
                jiCuadrado = 0.0
                total = numeros[21][i]
                
                for j in range(21):
                        if(j != 20):
                            promedio = t*(1 - t)**j
                        else:
                            promedio = (1-t)**j
                        frecuenciaObs = numeros[j][i]
                        frecuenciaEsperada = promedio*total
                        fObs_fEsp = frecuenciaObs-frecuenciaEsperada
                        fObs_fEsp2 = fObs_fEsp**2
                        formula = fObs_fEsp2/frecuenciaEsperada
                 
                        fila = (j,promedio,frecuenciaObs,frecuenciaEsperada,fObs_fEsp,fObs_fEsp2,formula)

                        jiCuadrado +=formula
                        table.add_hline()
                        table.add_row(fila)

            doc.append( Math(data=['Intervalo','=',intervalo[i]]) )
            doc.append( Math(data=['X2(0)','=',jiCuadrado]) )

def mostrarPoker(poker, doc):
    with doc.create(Subsection('Prueba de Poker')):
        nombrePoker = ["Diferentes","Un Par","Dos Pares","Tercia","Full House","Poker","Quintilla"]
        promedio = [0.3024,0.504,0.108,0.072,0.009,0.0045,0.0001]
        total = pruebasRandom.sumarPoker(poker)
        jiCuadrado = 0.0

        with doc.create(Tabular('|c|c|c|c|c|c|c|')) as table:
            table.add_row(('Clase','Pe','Fo','Fe','(Fo-Fe)','(Fo-Fe)2','(Fo-Fe)2/Fe'))
            for i in range(7):
                prom = promedio[i]
                frecuenciaObs = poker[i][1]
                frecuenciaEsperada = prom*total
                fObs_fEsp = frecuenciaObs-frecuenciaEsperada
                fObs_fEsp2 = fObs_fEsp**2
                formula = fObs_fEsp2/frecuenciaEsperada
                 
                fila = (nombrePoker[i],prom,frecuenciaObs,frecuenciaEsperada,fObs_fEsp,fObs_fEsp2,formula)

                table.add_hline()
                table.add_row(fila)

                jiCuadrado += formula

        doc.append( Math(data=['Total','=',total]) )
        doc.append( Math(data=['X2(0)','=',jiCuadrado]) )

def mostrarSeries(series,doc):
    with doc.create(Subsection('Prueba de Series')):
        n = pruebasRandom.sumarLista(series)
        frecuenciaEsperada = n/25
        jiCuadrado = 0.0

        with doc.create(Tabular('|c|c|c|c|c|c|')) as table:
            table.add_row(('Celda','Fo','Fe','(Fo-Fe)','(Fo-Fe)2','(Fo-Fe)2/Fe'))

            for i in range(25):
                frecuenciaObs = series[i]
                fObs_fEsp = frecuenciaObs-frecuenciaEsperada
                fObs_fEsp2 = fObs_fEsp**2

                formula = fObs_fEsp2/frecuenciaEsperada

                fila = (i+1,frecuenciaObs,frecuenciaEsperada,fObs_fEsp,fObs_fEsp2,formula)

                table.add_hline()
                table.add_row(fila)
        
                jiCuadrado += (frecuenciaObs-frecuenciaEsperada)**2/frecuenciaEsperada
                
    doc.append( Math(data=['Total','=',(int)(n)]) )
    doc.append( Math(data=['X2(0)','=',jiCuadrado]) )  

if __name__ == '__main__':
    t = datetime.today()
    print(t)
    doc = Document()
    doc.preamble.append(Command('title', 'Tarea 2 Simulacion'))
    doc.preamble.append(Command('author', 'Mauricio Arce Fernandez'))
    doc.preamble.append(Command('date', NoEscape(r'\today')))
    doc.append(NoEscape(r'\maketitle'))
    
    archivo = ["randomHaskell.txt","randomJava.txt","randomPerl.txt", "randomPython.txt", "randomProlog.txt", "randomScala.txt", "randomORG.txt"]
    nombre = ["Haskell", "Java", "Perl", "Python", "Prolog","Scala","Random.org"]

    for i in range(7):
        doc.append(Section(nombre[i]))
        resultadoPromedio = pruebasRandom.promedio(archivo[i])
        resultadoVarianza = pruebasRandom.varianza(archivo[i], resultadoPromedio)
        resultadoCorridas = pruebasRandom.pruebaCorridas(archivo[i])
        resultadoDigitos = pruebasRandom.obtenerCadena(archivo[i])
        resultadoPoker = pruebasRandom.obtenerPoker(archivo[i])
        resultadoSerie = pruebasRandom.obtenerSeries(archivo[i])

        
        mostrarPromedio(resultadoPromedio, doc)
        mostrarVarianza(resultadoVarianza, doc)
        mostrarCorridas(resultadoCorridas, doc)
        
        mostrarDigitos(resultadoDigitos, doc)
        mostrarNumeros(archivo[i],doc)
        mostrarPoker(resultadoPoker, doc)
        mostrarSeries(resultadoSerie, doc)
        print(nombre[i])

    doc.generate_pdf('resultadoPruebas', clean_tex=False)
    t = datetime.today()
    print(t)
