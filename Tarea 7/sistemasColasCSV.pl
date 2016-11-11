my $archivoLectura = @ARGV[0];
my $archivoEscritura = @ARGV[1];

multicola($archivoLectura,$archivoEscritura);

sub multicola($archivoLectura, $archivoEscritura){
    my @datos = leerArchivo($archivoLectura);
    my $tablaFinal = "Resultado Colas";
   
    for(my $i = 0; $i < scalar(@datos); $i++){
        my $tabla = @datos[$i];
        $tabla =~ s/[\(][\(][\()]/###/g;
        $tabla =~ s/[\s][\(][\()]/###/g;
        $tabla =~ s/[\(][\()]/##/g;
        $tabla =~ s/[\s][\(]/,/g;
        $tabla =~ s/[\)][\)]/\n/g;
        $tabla =~ s/[\)][\s]/,/g;
        $tabla =~ s/[\)]//g;
        $tabla =~ s/\n,/\n;/g;
        $tabla =~ s/\s/:/g;
        $tabla =~ s/:;/\n/g;
        $tabla =~ s/:$/\n/g;
        $tabla =~ s/:###/###/g;
        $tabla =~ s/###/\n\n# Etapa,Hora de Llegada,Tiempo de Atención,Hora de Atención,Hora de Salida, Tiempo de Espera, Tiempo en el Sistema\n/g;
        $tabla =~ s/##/\n\n# Cola,Hora de Llegada,Tiempo de Atención,Hora de Atención,Hora de Salida, Tiempo de Espera, Tiempo en el Sistema\n/g;
        $tablaFinal = $tablaFinal . $tabla;
    }
    open(FH, '>', $archivoEscritura) or die("No se pudo abrir el archivo!!");
    print FH $tablaFinal;
    close(FH);
}

sub leerArchivo($archivoLectura){
    open(FILER, $archivoLectura) or die("No se pudo abrir el archivo!!");
    @datos=<FILER>;
    close(FILER);
    return @datos;
}


