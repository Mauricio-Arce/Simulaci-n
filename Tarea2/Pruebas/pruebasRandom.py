import math

def varianza(direccion, promedio):
        archivo = open(direccion,'r')
        linea = archivo.readline()

        sumatoriaRandoms = 0.0
        resultadoParcial = 0.0
        xi = 0.0
        n = 0

        while(linea!=""):
                xi = (float) (linea)
                resultadoParcial = (xi - promedio)**2
                sumatoriaRandoms += resultadoParcial
                linea = archivo.readline()
                n +=1
                
        archivo.close()
        varianza = sumatoriaRandoms/(n - 1)
        resultadoVarianza = [varianza,math.sqrt(varianza)]

        return resultadoVarianza

def promedio(direccion):
        archivo = open(direccion,'r')
        linea = archivo.readline()
    
        sumatoriaRandoms = 0.0
        n = 0
    
        while(linea!=""):
                sumatoriaRandoms += (float)(linea)
                linea = archivo.readline()
                n +=1
        
        archivo.close()
        promedio = sumatoriaRandoms/n
        
        return promedio

#####################################################################################
##
##Funcion para la prueba por corridas
##
#####################################################################################

def pruebaCorridas(direccion):
        archivo = open(direccion,'r')
        linea = archivo.readline()

        randomActual = linea
        signoActual = "*"
        cantidadCorridasH = 0
        n = 0
        
        while(linea!=""):
                linea = archivo.readline()
                if(randomActual <= linea and (signoActual == "-" or signoActual == "*")):
                        cantidadCorridasH +=1
                        signoActual = "+"
                elif(randomActual > linea and (signoActual == "+" or signoActual == "*")):
                        cantidadCorridasH +=1
                        signoActual = "-"
                randomActual = linea
                n +=1
                        
        archivo.close()

        eH = (2*n -1)/3
        sH2 = (16*n - 29)/90
        sH = math.sqrt(sH2)
        zObservado = (cantidadCorridasH - eH)/sH
        resultadoCorridas = [cantidadCorridasH, eH, sH2, sH, zObservado]

        return resultadoCorridas

##########################################################################################
##
##Funciones para las pruebas por huecos con digitos
##
##########################################################################################
       
def obtenerCadena(direccion):
        archivo = open(direccion,'r')
        linea = archivo.readline()
        largo = len(linea) -1
        cadenaDigitos = linea[2:largo]

        while(linea!=""):
                linea = archivo.readline()
                largo = len(linea)-1
                cadenaDigitos += linea[2:largo]                   
        archivo.close()
        return contarHuecosDigitos(cadenaDigitos)

def contarHuecosDigitos(cadenaNumeros):
        largoCadena = len(cadenaNumeros) 
        tablaDigitos = generarMatriz(20,10)
        digitos = [0]*10
        primeraAparicion = [True]*10
        
        for i in range(largoCadena):
                digitoActual = (int)(cadenaNumeros[i])
                for j in range(10):
                        if(digitoActual != j):
                                digitos[j] += 1

                if(digitos[digitoActual] < 20 and primeraAparicion[digitoActual] or digitos[digitoActual] >= 20 and primeraAparicion[digitoActual]):
                        primeraAparicion[digitoActual] = False

                elif(digitos[digitoActual] < 20 and primeraAparicion[digitoActual] == False):
                        tablaDigitos[digitos[digitoActual]][digitoActual] += 1

                elif(digitos[digitoActual] >= 20 and primeraAparicion[digitoActual] == False):
                        tablaDigitos[20][digitoActual] += 1
                                
                digitos[digitoActual] = 0
                        

        return sumarTablas(tablaDigitos,20,10,False)

def generarMatriz(filas,columnas):
        matriz = []*filas
        for i in range(filas+1):
                a = []
                for j in range(columnas+1):
                        a.append(0)
                matriz.append(a)
        return matriz
       

def sumarTablas(matriz, filas, columnas, bandera):
        total = 0
        for i in range(filas+1):
                contadorFila = 0;
                for j in range(columnas+1):
                        if(j==columnas):
                                matriz[i][j] = contadorFila
                                total += contadorFila
                        else:
                                contadorFila += matriz[i][j]    
        
        if(bandera):
                return total
        else:
                return matriz
        
####################################################################################
##
##Funciones para pruebas de huecos con numeros
##
####################################################################################

def contarHuecosNumeros(cadenaNumeros,intervalo):        
        largoCadena = len(cadenaNumeros) 
        tablaIntervalos = generarMatriz(21,5)
        intervalosDiferentes = [0]*5
        intervaloActual = 0
        primeraAparicion = [True]*5
        
        for i in range(largoCadena):
                randomActual = (float)(cadenaNumeros[i])

                for j in range(5):
                        if(randomActual >= intervalo[j][0] and randomActual <= intervalo[j][1]):
                                intervaloActual = j
                        else:
                                intervalosDiferentes[j] += 1

                if(intervalosDiferentes[intervaloActual] < 20 and primeraAparicion[intervaloActual] or intervalosDiferentes[intervaloActual] >= 20 and primeraAparicion[intervaloActual]):
                        primeraAparicion[intervaloActual] = False

                elif(intervalosDiferentes[intervaloActual] < 20 and primeraAparicion[intervaloActual] == False):
                        tablaIntervalos[intervalosDiferentes[intervaloActual]][intervaloActual] += 1

                elif(intervalosDiferentes[intervaloActual] >= 20 and primeraAparicion[intervaloActual] == False):
                        tablaIntervalos[20][intervaloActual] += 1
                                
                intervalosDiferentes[intervaloActual] = 0

        return sumarNumeros(tablaIntervalos,21,5)

def obtenerRandom(direccion,intervalo):
        archivo = open(direccion,'r')
        linea = archivo.readline()

        cadenaNumeros = []

        while(linea!=""):
                cadenaNumeros.append(linea.replace("\n",""))
                linea = archivo.readline()
        archivo.close()
    
        return contarHuecosNumeros(cadenaNumeros,intervalo)


def sumarNumeros(tablaNumeros,filas, columnas):
        total = 0
        for i in range(columnas+1):
                contadorColumna = 0;
                for j in range(filas+1):
                        if(j==filas):
                                tablaNumeros[j][i] = contadorColumna
                        else:
                                contadorColumna += tablaNumeros[j][i]    
        
        return tablaNumeros

#####################################################################################################################################
#
#Funciones para Pruebas de Poker
#
#####################################################################################################################################

def obtenerPoker(direccion):
        archivo = open(direccion,'r')
        linea = archivo.readline()
        cadenaPoker = []
        cadenaPoker.append(linea[:7])

        while(linea!=""):
                linea = archivo.readline()
                cadenaPoker.append(linea[:7])
        archivo.close()

        return contarPoker(cadenaPoker)

def contarPoker(cadenaPoker):
        tablaPoker = [["diferentes",0],["par",0],["2pares",0],["tercia",0],["full",0],["poker",0],["quintilla",0]]
        largoCadena = len(cadenaPoker) -1
 
        for i in range(largoCadena):
                poker = cadenaPoker[i]
                
                if(quintilla(poker)):
                        tablaPoker[6][1] +=1

                elif(randomPoker(poker)):
                        tablaPoker[5][1] +=1

                elif(fullHouse(poker)):
                        tablaPoker[4][1] +=1

                elif(tercia(poker)):
                        tablaPoker[3][1] +=1

                elif(pares(poker)):
                        tablaPoker[2][1] +=1

                elif(par(poker)):
                        tablaPoker[1][1] +=1

                else:
                        tablaPoker[0][1] +=1
                
        return tablaPoker

def par(poker):
        decimales = sorted(poker[2:])

        for i in range(4):
                sig = i+1
                if(decimales[i] in decimales[sig:]):
                        return True
                    
        return False

def pares(poker):
        decimales = sorted(poker[2:])

        if((decimales[0] == decimales[1]) and (decimales[2] == decimales[3]) ):
                return True
        
        elif((decimales[0] == decimales[1]) and (decimales[3] == decimales[4]) ):
                return True
        
        elif((decimales[1] == decimales[2]) and (decimales[3] == decimales[4]) ):
                return True
        
        return False

def tercia(poker):
        decimales = sorted(poker[2:])

        if((decimales[0] == decimales[1]) and (decimales[0] == decimales[2]) ):
                return True
        
        elif((decimales[1] == decimales[2]) and (decimales[1] == decimales[3]) ):
                return True
        
        elif((decimales[2] == decimales[3]) and (decimales[2] == decimales[4]) ):
                return True

        return False

def fullHouse(poker):
        decimales = sorted(poker[2:])
        
        if((decimales[0] == decimales[1]) and (decimales[0] == decimales[2]) and (decimales[3] == decimales[4])):
                return True
        
        elif((decimales[0] == decimales[1]) and (decimales[2] == decimales[3]) and (decimales[2] == decimales[4])):
                return True

        return False

def randomPoker(poker):
        decimales = sorted(poker[2:])
        
        if((decimales[1] == decimales[2]) and (decimales[1] == decimales[3]) and (decimales[1] == decimales[4]) ):
                return True
        
        elif((decimales[0] == decimales[1]) and (decimales[0] == decimales[2]) and (decimales[0] == decimales[3]) ):
                return True

        return False

def quintilla(poker):
        decimales = sorted(poker[2:])
        i = 1

        while(i < 5):
                if(decimales[0]!= decimales[i]):
                        return False
                i +=1

        return True

def sumarPoker(cadena):
        total = 0
        
        for i in range(len(cadena)):

                total += cadena[i][1]
                
        return total



##########################################################################################################################################################
#
#Funciones para la prueba de Serie
#
##########################################################################################################################################################

def obtenerSeries(direccion):
        archivo = open(direccion,'r')
        linea = archivo.readline()

        lineaActual = linea

        conjunto = []
        cadenaSeries = []

        while(linea!=""):
                linea = archivo.readline()
                conjunto.append(lineaActual.replace("\n",""))
                conjunto.append(linea.replace("\n",""))
                cadenaSeries.append(conjunto)
                lineaActual = linea
                conjunto = []
        archivo.close()
        cadenaSeries.pop() #Considera la ultima linea con un espacio vacio, entonces se quita esta opcion
        return contarSeries(cadenaSeries)

def contarSeries(cadenaSeries):
        celdas = generarCeldas()
        aparicionesCeldas = [0]*25
        largoCadena = len(cadenaSeries)

        for i in range(largoCadena):
                for j in range(25):
                        x = (float)(cadenaSeries[i][0])
                        y = (float)(cadenaSeries[i][1])
                        if(celdas[j][0] <= x and celdas[j][1] <= y and celdas[j][2] > x and celdas[j][3] > y):
                                aparicionesCeldas[j] +=1
                                break
        return aparicionesCeldas
       

def generarCeldas():
        celdas = []
        limiteSuperior = [0.2,0.4,0.6,0.8,1.0]
        limiteInferior = [0.0,0.2,0.4,0.6,0.8]
        
        for i in range(5):
                celdaActual = []         
                for j in range(5):
                        celdaActual.append(limiteInferior[i])
                        celdaActual.append(limiteInferior[j])
                        celdaActual.append(limiteSuperior[i])
                        celdaActual.append(limiteSuperior[j])
                        celdas.append(celdaActual)
                        celdaActual = []

        return celdas

def sumarLista(lista):
        resultado = 0.0
        
        for i in range(len(lista)):
                resultado +=lista[i]
                
        return resultado
