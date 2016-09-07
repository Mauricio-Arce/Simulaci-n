#Random en perl

open(ARCHIVO,">","/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomPerl.txt") || die "Error: El archivo no se pudo abrir\n";
my $indice = 0;
my $numeroAleatorio;

while($indice != 1000000){
    $numeroAleatorio = rand();
    printf ARCHIVO "%.16f\n", "$numeroAleatorio";
    $indice +=1;
}
close(ARCHIVO);
