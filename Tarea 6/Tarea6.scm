#lang scheme
;;Definiciones globales

(define e 2.718281828)
(define pi 3.14159265359)
(define particion 1/10)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;Distribuciones de Probabilidad
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define exponencial
  (lambda (lam)
    (lambda (x)
      (cond ((< x 0) 0)
            (else (* lam (exp (* -1 lam x))))))))

(define cuad
  (lambda (x)
    (* x x)))

(define normal
  (lambda (media desv)
    (lambda (x)
      (/ (exp (* -1/2 (cuad (/ (- x media) desv))))
         (* desv (sqrt (* 2 pi)))))))

(define fact
  (lambda (x)
    (cond ((zero? x) 1)
          (else (* x (fact (- x 1)))))))

(define poisson
  (lambda (lam)
    (lambda (x)
      (/ (* (exp (- lam))
            (expt lam x))
         (fact x)))))

;; Lista de n a 1
(define iota
  (lambda (n)
    (cond ((zero? n) '())
          (else (cons n (iota (- n 1)))))))

;; Coeficiente binomial
(define cb
  (lambda (n k)
    (apply *
     (map
      (lambda (i) (/ (- n i -1) i))
      (iota (- n k))))))

(define binomial
  (lambda (n p)
    (lambda (k)
      (* (cb n k) (expt p k) (expt (- 1 p) (- n k))))))

(define geometrica
  (lambda (p)
    (lambda (x)
      (* p (expt (- 1 p) (- x 1))))))

(define tabla
  (lambda (valores probabilidades)
    (lambda (x)
      (cadr (assoc x (map (lambda (x y) (list x y)) valores probabilidades))))))

(define nveces
  (lambda (n)
    (lambda (m)
      (* n m))))

(define doble
  (nveces 2))

(define exponencial-2
  (exponencial 2))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;                                                                                                                       ;;
;;Código generador de variables Aleatorias                                                                               ;;
;;                                                                                                                       ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Devuelve guarda la lista con los valores aleatorios y el rango respectivo 
(define gva
  (lambda (distribucion inicio fin)
   (vc distribucion inicio fin)))
        
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Variables Aleatorias Continuas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define vc
  (lambda (distribucion inicio fin)
    (define intervalo (intervalo-continuo inicio fin))
    (define mapeo (mapeo-simpson inicio fin distribucion))
    (define ran-inicial (random-continuo inicio fin))
    (vc-aux mapeo ran-inicial inicio fin)))

(define vc-aux
  (lambda (mapeo rand inicio fin)
    (cond ((eq? (pertenece mapeo rand) #t) rand)
          (else (vc-aux mapeo (random-continuo inicio fin) inicio fin)))))

(define pertenece
  (lambda (mapeo rand)
    (cond ((eq? mapeo '()) #f)
          (else (cond ((< (car mapeo) rand) #t)
                  (else (pertenece (cdr mapeo) rand)))))))

(define random-continuo
  (lambda (inicio fin)
    (+ inicio (* (- fin inicio) (random)))))

;;Evalua cada intervalo en simpson
(define mapeo-simpson
  (lambda (inicio fin distribucion)
    (define intervalo (intervalo-continuo inicio fin))
    (define resultados (sort (map (lambda (x) (integral distribucion inicio (cadr x))) intervalo) <))
    resultados))
    
;;Genera los intervalos de las variables continuas
(define intervalo-continuo
  (lambda (inicio fin)
    (cond ((eq? inicio fin) '())
          (else (cons (cons (/ inicio 1.0) (cons (+ inicio 0.01) '())) (intervalo-continuo (+ inicio particion) fin))))))

(define simpson
  (lambda (n)
    (lambda (fun a b)
      (let* ((h (/ (- b a) n))
            (lista (append (list (fun a) (fun b))
                           (map (lambda(i)
                                  (cond ((even? i) (* 2 (fun(+ a (* i  h)))))
                                        (else (* 4 (fun (+ a (* i h)))))))
                           (iota (- n 1))))))
        (* (/ h 3) (apply + lista))))))

(define integral
  (simpson 1000))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;Tarea 6
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(sistemas-colas #t (geometrica 0.3) '(0 3) (exponencial 3) '(0 4) '(8 0 0) '(9 0 0) 1) unicola
;;(sistemas-colas #f (normal 0 0.5) '(1 3) (exponencial 0.5) '(0 3) '(8 0 0) '(10 0 0) 2)  multicola
(define sistemas-colas
  (lambda (es-unicola distribucionTLL rangoTLL distribucionTA rangoTA horaInicio horaFin servidores)
    (cond ((eq? es-unicola #t)
           (define horas-llegada (unicolaHLL horaInicio horaFin distribucionTLL (first rangoTLL) (second rangoTLL) 1))
           (define tiempo-atencion (tiempoAtencion distribucionTA (first rangoTA) (second rangoTA) (length horas-llegada)))
           (define horas (unirHLL-TA horas-llegada tiempo-atencion))
           (unicola horas servidores "unicola.txt"))
          (else
             (define horas-llegada (unicolaHLL horaInicio horaFin distribucionTLL (first rangoTLL) (second rangoTLL) servidores))
             (define tiempo-atencion (tiempoAtencion distribucionTA (first rangoTA) (second rangoTA) (length horas-llegada)))
             (define horas (unirHLL-TA horas-llegada tiempo-atencion))
             (multicola horas servidores "multicola.txt")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;Código Unicola
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define unicola
  (lambda (tablaTiempo servidores archivo)
    (define primeros (primerosAtendidos tablaTiempo servidores))
    (define tablaActualizada (quitarPrimeros tablaTiempo servidores))
    (define primeros-atendidos (salidas primeros))
    
    (define tabla-temporal (unicola-aux primeros tablaActualizada (last primeros) primeros-atendidos servidores))
    (define TPAyVTA (atencion tabla-temporal))

    (define tiempo-espera (espera tabla-temporal))
    (define TPEyVTE (TPVT tiempo-espera))
    (define tiempoColEspera (unirColumnas tabla-temporal tiempo-espera))

    (define tiempo-sistema (sistema tabla-temporal))
    (define TPSyVTS (TPVT tiempo-sistema))
    (define tabla (unirColumnas tiempoColEspera tiempo-sistema))
    (escribir-archivo (reverse tabla) archivo)
    (descripcionSalida TPAyVTA TPEyVTE TPSyVTS)))

(define unicola-aux
  (lambda (tablaFinal tablaTemporal anterior listaSalida servidores)
    (cond ((eq? tablaTemporal '()) tablaFinal)
           (else (define actual-cola (car tablaTemporal))
                 (define hora-atendido (horaAtencion (second actual-cola) listaSalida servidores))
                 (define actual-atendido (cons hora-atendido (list (ingresarTiempo hora-atendido (third actual-cola)))))
                 (define actual (append actual-cola actual-atendido))
                 (define nuevos-atendidos (cons (ingresarTiempo hora-atendido (third actual-cola)) listaSalida))
                 (define nueva-Tabla (cons actual tablaFinal))
                 (unicola-aux nueva-Tabla (cdr tablaTemporal) actual nuevos-atendidos servidores)))))

(define primerosAtendidos
  (lambda (cola n)
    (cond ((zero? n) '())
          (else
           (define primero-cola (car cola))
           (define primero-atendido (cons (second primero-cola) (append (list (ingresarTiempo (second primero-cola) (third primero-cola))))))
           (define primero (append primero-cola primero-atendido))
           (cons primero (primerosAtendidos (cdr cola) (- n 1)))))))

(define quitarPrimeros
  (lambda (tabla n)
    (cond ((zero? n) tabla)
         (else (quitarPrimeros (cdr tabla) (- n 1))))))

(define salidas
  (lambda (primeros)
    (cond ((eq? primeros '()) '())
          (else
           (define salida-Actual (ingresarTiempo (second (first primeros)) (third (first primeros))))
           (cons salida-Actual (salidas (cdr primeros)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;MultiCola
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define multicola
  (lambda (horas servidores archivo)
    (define lista-servidores (map (lambda (x) '()) (iota servidores)))
    (define tablas (multicola-aux horas lista-servidores))
    (define tespera (map (lambda (x) (espera x)) tablas))
    (define tsistema (map (lambda (x) (sistema x)) tablas))
    (define tablaFinal (unirTiemposMC tablas tespera tsistema))
    (escribir-archivo tablaFinal archivo)
    
    (define TPAyVTA (atencion (partirLista tablaFinal)))
    (define TPEyVTE (TPVT (partirLista tespera)))
    (define TPSyVTS (TPVT (partirLista tsistema)))
    (descripcionSalida TPAyVTA TPEyVTE TPSyVTS)))
    ;;(define TPAyVTA (map (lambda (x) (atencion x)) tablaFinal))
    ;;(define TPEyVTE (map (lambda (x) (TPVT x)) tespera))
    ;;(define TPSyVTS (map (lambda (x) (TPVT x)) tsistema))
    ;;(salidaMulticola TPAyVTA TPEyVTE TPSyVTS 1)))

(define multicola-aux
  (lambda (horas servidores)
    (cond ((eq? horas '()) servidores)
          (else
           (define servidorActualizado (colocarElemento servidores (first horas)))
           (multicola-aux (cdr horas) servidorActualizado)))))

(define partirLista
  (lambda (lista)
    (cond ((eq? lista '()) '())
          (else (append (first lista) (partirLista (cdr lista)))))))
  
(define salidaMulticola
  (lambda (tiempoAtencion tiempoEspera tiempoSistema cantServidores)
    (cond ((eq? (length tiempoAtencion) 1)
           (println (list "Servidor" cantServidores))
           (descripcionSalida (first tiempoAtencion) (first tiempoEspera) (first tiempoSistema)))
          (else
            (println (list "Servidor" cantServidores))
            (descripcionSalida (first tiempoAtencion) (first tiempoEspera) (first tiempoSistema))
            (newline)
            (salidaMulticola (cdr tiempoAtencion) (cdr tiempoEspera) (cdr tiempoSistema) (+ cantServidores 1))))))
          
(define unirTiemposMC
  (lambda (tabla tespera tsistema)
    (cond ((eq? tabla '()) '())
          (else
           (define tablaE (unirColumnas (first tabla) (first tespera)))
           (define tablaServidor (unirColumnas tablaE (first tsistema)))
           (cons tablaServidor (unirTiemposMC (cdr tabla) (cdr tespera) (cdr tsistema)))))))
  
(define pesosIguales
  (lambda (listaServidores pesoInicial)
    (cond ((eq? listaServidores '()) #t)
          (else (cond ((not (eq? pesoInicial (length (first listaServidores)))) #f)
                 (else (pesosIguales (cdr listaServidores) pesoInicial)))))))

(define ingresarHPI
  (lambda (listaServidores cantidad hora)
    (define posicionAleatoria (random cantidad))
    (define nuevaLista (ingresarHS listaServidores hora posicionAleatoria 0))
    nuevaLista))
         
(define ingresarHS
  (lambda (servidores valor posEsp posAct)
    (cond ((eq? posEsp posAct)
           (cond ((eq? (car servidores) '()) (cons (list valor) (cdr servidores)))
                 (cons (cons (append (car servidores) (list valor)) (cdr servidores)))))
          (else (cons (first servidores) (ingresarHS (cdr servidores) valor posEsp (+ posAct 1)))))))

(define desocupados
  (lambda (servidores entrante)
    (define posiciones (posicionesDesocupados servidores entrante '() 0))
    (cond ((eq? (length posiciones) 1) (first posiciones))
          (else
           (define posicionAleatoria (random (- (length posiciones) 1)))
           (list-ref posiciones posicionAleatoria)))))
  
(define posicionesDesocupados
  (lambda (servidores horallegada posiciones posAct)
    (cond ((eq? servidores '()) (reverse posiciones))
          (else (cond ((eq? (first servidores) '())
                       (posicionesDesocupados (cdr servidores) horallegada (append (list posAct) posiciones) (+ posAct 1)))
                      (else (cond ((< (horasAsegundos (first servidores)) (horasAsegundos horallegada)) 
                       (posicionesDesocupados (cdr servidores) horallegada (append (list posAct) posiciones) (+ posAct 1)))
                                  (else (posicionesDesocupados (cdr servidores) horallegada posiciones (+ posAct 1))))))))))

(define servidoresDesocupados
  (lambda (servidores entrante)
    (cond ((eq? servidores '()) #f)
          (else (cond ((eq? (first servidores) '()) #t)
                      (else (cond ((< (horasAsegundos (first servidores)) (horasAsegundos entrante)) #t)
                                  (else (servidoresDesocupados (cdr servidores) entrante)))))))))

(define ultimos
  (lambda (servidores)
    (cond ((eq? servidores '()) '())
          (else
           (cond ((eq? (first servidores) '()) (cons '() (ultimos (cdr servidores))))
                 (else (cons (fifth (last (first servidores))) (ultimos (cdr servidores)))))))))

(define colocarElemento
  (lambda(servidores entrante)
    (define ultimos-atendidos (ultimos servidores))
    (define cola-activa (map (lambda (x) (colaActual x (second entrante))) servidores))
    (cond ((eq? (servidoresDesocupados ultimos-atendidos (second entrante)) #t)
           ;;(println "Desocupado")
           (define posicionDesocupado (desocupados ultimos-atendidos (second entrante)))
           (define atendido (primerosAtendidos (list entrante) 1))
           (ingresarHS servidores (cons posicionDesocupado (cdar atendido)) posicionDesocupado 0))
          (else
           ;;(println "Ocupado")
           (define posicionMenor (menorCola cola-activa))
           (define hora-atencion (list-ref ultimos-atendidos posicionMenor))
           (define hora-salida (ingresarTiempo hora-atencion (third entrante)))
           (define atendido (list posicionMenor (second entrante) (third entrante) hora-atencion hora-salida))
           (ingresarHS servidores atendido posicionMenor 0)))))

(define colaActual
  (lambda (cola horaLimite)
    (cond ((eq? cola '()) '())
          (else (cond ((< (horasAsegundos (fifth (first cola))) (horasAsegundos horaLimite)) (colaActual (cdr cola) horaLimite))
                      (else (cons (first cola) (colaActual (cdr cola) horaLimite))))))))
(define menorCola
  (lambda (tabla)
    (define colas-menores (menorCola-aux (cdr tabla) '(0) 1 (length (first tabla))))
      (cond ((eq? (length colas-menores) 1) (first colas-menores))
            (else
             (define posicionAleatoria (random (length colas-menores)))
             (list-ref colas-menores posicionAleatoria)))))
  
(define menorCola-aux
  (lambda (tabla posMenor posActual largo)
    (cond ((eq? tabla '()) (reverse posMenor))
          (else (cond ((eq? (length (first tabla)) largo)
                       (menorCola-aux (cdr tabla) (append (list posActual) posMenor) (+ posActual 1) largo))
                      (else (cond ((< (length (first tabla)) largo)
                                   (menorCola-aux (cdr tabla) (list posActual) (+ posActual 1) (length (first tabla))))
                                  (else (menorCola-aux (cdr tabla) posMenor (+ posActual 1) largo)))))))))

(define descripcionSalida
  (lambda (atencion espera sistema)
    (print "Tiempo Promedio de Atención:     ")
    (println (castear-hora (car atencion)))
    (print "Varianza del Tiempo de Atención: ")
    (println (castear-hora (cadr atencion)))
    (print "Tiempo Promedio de Espera:       ")
    (println (castear-hora (car espera)))
    (print "Varianza del Tiempo de Espera:   ")
    (println (castear-hora (cadr espera)))
    (print "Tiempo Promedio de Sistema:      ")
    (println (castear-hora (car sistema)))
    (print "Varianza del Tiempo de Sitema:   ")
    (println (castear-hora (cadr sistema)))))

(define castear-hora
  (lambda (hora)
    (cond ((>= (car hora) 24)
           (define dias (floor (inexact->exact (/ (car hora) 24))))
           (define hora-correcta (- (car hora) (* dias 24)))
           (append (list dias hora-correcta) (cdr hora)))
          (else hora))))
                   
(define atencion
  (lambda (tabla)
    (define tiempo (map (lambda (x) (third x)) tabla))
    (define prom (apply + tiempo))
    (define var (/ (apply +(map (lambda (x) (expt (- x prom) 2)) tiempo)) (- (length tiempo) 1)))
    (list (segundosAhoras prom) (segundosAhoras var))))

(define TPVT
  (lambda (lista)
    (define prom (promedio lista))
    (define var (varianza lista prom))
    (list prom var)))

(define espera
  (lambda (tabla)
    (map (lambda (x) (tiempoES (second x) (fourth x))) tabla)))

(define sistema
  (lambda (tabla)
    (map (lambda (x) (tiempoES (second x) (fifth x))) tabla)))

(define unirHLL-TA
  (lambda (horas tiempo)
    (cond ((eq? tiempo '()) '())
          (else (cons (append (list (caar horas)) (append (list (cdar horas)) (list (car tiempo)))) (unirHLL-TA (cdr horas) (cdr tiempo)))))))

(define unirColumnas
  (lambda (horas tiempo)
    (cond ((eq? tiempo '()) '())
          (else (cons (append (first horas) (list (first tiempo))) (unirColumnas (cdr horas) (cdr tiempo)))))))
 
(define promedio
  (lambda (lista)
    (define prom (/ (apply +(map (lambda (x) (horasAsegundos x)) lista)) (length lista)))
    (segundosAhoras prom)))

(define varianza
  (lambda (lista prom)
    (define var (/ (apply +(map (lambda (x) (expt (- (horasAsegundos  x) (horasAsegundos prom)) 2)) lista)) (- (length lista) 1)))
    (segundosAhoras var)))

(define tiempoES
  (lambda (horaLlegada horaES)
    (segundosAhoras (- (horasAsegundos horaES) (horasAsegundos horaLlegada)))))

(define tiempoAtencion
  (lambda (distribucion a b n)
    (cond ((zero? n) '())
          (else (cons (gva distribucion a b) (tiempoAtencion distribucion a b (- n 1)))))))

(define horaSalida
  (lambda (horasLlegada tiemposAtencion)
    (cond ((eq? horasLlegada '()) '())
          (else
           (define hora-salida (ingresarTiempo (cdar horasLlegada) (car tiemposAtencion)))
           (cons hora-salida (horaSalida cdr horasLlegada cdr tiempoAtencion))))))

(define horaAtencion
  (lambda (horaLlegada horasSalida n)
    (define horaSalidaMin (horaMinima horasSalida n))
    (cond ((eq? (horasAsegundos horaLlegada) (max (horasAsegundos horaLlegada) (horasAsegundos horaSalidaMin))) horaLlegada)
          (else horaSalidaMin))))

(define unicolaHLL
  (lambda (horaInicio horaFin distribucion a b n)
    (map (lambda (x) (cons (first x) (segundosAhoras (second x)))) (unicolaHLL-aux horaInicio horaFin distribucion a b n))))

(define unicolaHLL-aux
  (lambda (horaInicio horaFin distribucion a b n)
    (cond ((zero? n) '())
          (else
           (define horas-llegada (sort
                                  (append
                                        (map (lambda (x) (append (list n) (list (horasAsegundos x))))
                                             (horaLlegada horaInicio horaFin distribucion a b))
                                               (unicolaHLL-aux horaInicio horaFin distribucion a b (- n 1))) #:key second <))
           horas-llegada))))

(define horaLlegada
  (lambda (horaActual horaFin distribucion  a b)
    (define tiempo-llegada (gva distribucion a b))
    (define nueva-hora (ingresarTiempo horaActual tiempo-llegada))
    (cond ((eq? (< (horasAsegundos nueva-hora) (horasAsegundos horaFin)) #t)
           (cons nueva-hora (horaLlegada nueva-hora horaFin distribucion a b)))
          (else '()))))
         
(define ingresarTiempo
  (lambda (hora variable)
    (define decimales-minutos (- variable (floor (inexact->exact variable))))
    (define decimales-segundos (* decimales-minutos 60))
    (define minutos (+ (cadr hora) (floor (inexact->exact variable))))
    (define segundos (+ (caddr hora) (floor (inexact->exact decimales-segundos))))
    (define hora-act (cons (car hora) (cons minutos (cons segundos '()))))
    (cond ((eq? (and (>= minutos 60) (>= segundos 60)) #t)
           (define horas-act (+ (car hora) 1))
           (define minutos-act (- minutos 60))
           (define segundos-act (- segundos 60))
           (define hora-act (cons horas-act (cons minutos-act (cons segundos-act '()))))
           hora-act)
          (else (cond ((>= segundos 60)
                       (define minutos-act (+ minutos 1))
                       (define segundos-act (- segundos 60))
                       (define hora-act (cons (car hora) (cons minutos-act (cons segundos-act '()))))
                       hora-act)
                      (else (cond ((>= minutos 60)
                                   (define horas-act (+ (car hora) 1))
                                   (define minutos-act (- minutos 60))
                                   (define hora-act (cons horas-act (cons minutos-act (cons segundos '()))))
                                   hora-act)
                                  (else hora-act))))))))
                
(define horaMinima
  (lambda (horasSalida n)
    (cond ((eq? (length horasSalida) 0) 0)
          (else (horaMinima-aux (cdr horasSalida) (car horasSalida) (- n 1))))))

(define horaMinima-aux
  (lambda (horasSalida horaMinima n)
    (cond ( (or (zero? n) (eq? horasSalida '())) horaMinima)
          (else (cond ( (< (horasAsegundos (car horasSalida)) (horasAsegundos horaMinima))
                   (horaMinima-aux (cdr horasSalida) (car horasSalida) (- n 1)))
                 (else (horaMinima-aux (cdr horasSalida) horaMinima (- n 1))))))))

(define horasAsegundos
  (lambda (hora)
    (cond ((or (eq? hora '()) (eq? hora 0)) 0)
          (else (+ (* (car hora) 3600) (* (cadr hora) 60) (caddr hora))))))

(define segundosAhoras
  (lambda (segundos)
    (define horas (floor (inexact->exact (/ segundos 3600))))
    (define minutos (floor (inexact->exact (/ (- segundos (* horas 3600)) 60))))
    (define segundos-extra (+ (* horas 3600) (* minutos 60)))
    (define seg (floor (inexact->exact (- segundos segundos-extra))))
    (cons horas (cons minutos (cons seg '())))))


;;Escribir archivo
(define escribir-archivo
  (lambda (lista archivo)
       (call-with-output-file archivo #:exists 'truncate
        (lambda (salida)
          (display lista salida)))))