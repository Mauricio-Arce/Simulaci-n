##Random
import random

def main():
    i = 0
    archivo = open("/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomPython.txt","w")
    while(i<1000000):
        numeroRandom = (str)('{0:.16f}'.format(random.random()))
        archivo.write(numeroRandom + "\n")
        i+=1

    archivo.close()
