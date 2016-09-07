use File::Copy;
use HTML::TagParser;

my $comando = $ARGV[0];
my $archivoLectura = $ARGV[1];
my $archivoEscritura = "/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomORG.txt";
my $archivoHTML = "randomORG.html";

##Copia los random de un archivo txt al original
if($comando eq "cp"){

	open(my $Lectura,"<$archivoLectura");
	open(my $Escritura,">>$archivoEscritura");	
	
	while(<$Lectura>){
		chomp;
		print $Escritura "$_\n";
	}
	
	close($Lectura);
	close($Escritura);
}

##Parsea el html y copia los random en el archivo randomORG.txt
elsif($comando eq "pHTML"){
	$archivoHTML = $ARGV[1];
	parsearHTML();
}

##Se conecta a random.org, extrae el html segun la cantidad de numeros aleatorios y lo parsea para guardar los datos
else{	
	if($comando eq ""){
		$comando = 10000	
	}
	
	my $url = "https://www.random.org/decimal-fractions/?num=$comando&dec=16&col=1&format=html&rnd=new";
	my $textoPlano = qx{curl --silent $url}; 

	guardarDatos();	  	
	parsearHTML();
	
}

sub guardarDatos(){
	open(my $fileHTML,'>',$archivoHTML);
	print $fileHTML $textoPlano;
	close($fileHTML);
}

sub parsearHTML(){
	my $html = HTML::TagParser->new($archivoHTML);
	my @randoms = $html->getElementsByTagName("pre");
	my $elem = $html->getElementsByTagName("pre");

	open(my $Escritura,">>$archivoEscritura");
	my $text = $elem->innerText(), if ref $elem;		
	print $Escritura $text;
	close($Escritura);
}
