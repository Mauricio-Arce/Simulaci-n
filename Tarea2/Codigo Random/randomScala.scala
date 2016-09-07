//Random en Scala
import java.io._

val archivo = new File("/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomScala.txt")
val buffer = new BufferedWriter(new FileWriter(archivo))

var indice = 0 
val random = scala.util.Random

while(indice < 1000000){
    val numeroAleatorio = ("%1.16f".format(random.nextDouble)).toString
    
    buffer.write(numeroAleatorio.replace(",",".")+"\n")
    indice = indice + 1
}
buffer.close()
