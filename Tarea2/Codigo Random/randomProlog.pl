iniRandom():- Indice = 0, 
    open("/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomProlog.txt",write,Archivo), 
    randomProlog(Indice, Archivo), 
    close(Archivo). 
     
randomProlog(1000000, Archivo) :- !.
randomProlog(Indice, Archivo) :- SiguienteNumero is Indice + 1, 
    random(0.0,1.0,NumeroAleatorio),
    format(Archivo, '~16f', NumeroAleatorio),
    nl(Archivo),
    randomProlog(SiguienteNumero, Archivo).
