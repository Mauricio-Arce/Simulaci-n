#lang scheme

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

(define iota-aux
  (lambda (n acum)
    (cond ((zero? n) acum)
          (else (iota-aux (- n 1) (cons n acum))))))

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

;;Genera una serie de intervalos con su cantidad de ocurrencias en una muestra
(define conteo
  (lambda (muestra-ordenada intervalo)
    (conteo-aux muestra-ordenada
                (list (car muestra-ordenada)
                      (+ (car muestra-ordenada) intervalo)
                      0)
                intervalo)))

;;Cuenta los valores de la muestra con respecto a un intervalo
(define conteo-aux
  (lambda (muestra acum intervalo)
    (cond ((null? muestra) (list acum))
          ((< (car muestra) (cadr acum))
           (conteo-aux (cdr muestra)
                       (list (car acum) (cadr acum) (+ 1 (caddr acum)))
                       intervalo))
          (else
           (cons acum (conteo-aux muestra (list (cadr acum)
                                                (+ intervalo (cadr acum))
                                                0)
                                  intervalo))))))

;;Métodos principal para calcular el Ji Cuadrado de variables continuas y discretas
(define pruebaJiCuadrado
 (lambda (es-discreta distribucion archivo particion)
     (cond ( (eq? es-discreta #t)
             (define datos (car (leer-archivo archivo)))
             (define listaX-Fo (conteo (sort datos <) particion))
             (ji-discreto listaX-Fo distribucion))
           (else
            (lambda (a b)
            (define datos (car (leer-archivo archivo)))
            (define lista (crear-intervalo a b particion))
            (define frecuencia-observada (mapeo-continuas (sort datos <) lista))
            (ji-continuo frecuencia-observada distribucion))))))                                          

;;Crea un intervalo a partir de un a, un b y un espacio
(define crear-intervalo
  (lambda (a b espacio)
    (cond ((eq? a b) '())
          (else (cons (cons a (cons (+ a espacio) '())) (crear-intervalo (+ a espacio) b espacio))))))

;;Asocia un intervalo con sus ocurrencias
(define mapeo-continuas
  (lambda (muestra lista)
    (map (lambda (x) (append x (cons (contar-ocurrencias (car x) (cadr x) muestra 0) '()))) lista)))

;;Cuenta la cantidad de ocurrencias de un intervalo
(define contar-ocurrencias
  (lambda (a b muestra acumulado)
    (cond ((null? muestra) acumulado)
          ((pertenece a b (car muestra))
           (contar-ocurrencias a b (cdr muestra) (+ acumulado 1)))
          (else (contar-ocurrencias a b (cdr muestra) acumulado)))))

;;Genera el resultado del ji cuadrado para las discretas
(define ji-discreto
  (lambda (listaX-Fo distribucion)
    (define n (cantidad-n listaX-Fo))
    (apply +(map (lambda (x) (ji-discreto-aux distribucion (caddr x) (car x) n)) listaX-Fo))))

;;Genera el resultado del ji cuadrado para las continuas
(define ji-continuo
  (lambda (listaX1X2-Fo distribucion)
    (define n (cantidad-n listaX1X2-Fo))
    (apply +(map (lambda (x) (ji-continua-aux distribucion (caddr x) (car x) (cadr x) n)) listaX1X2-Fo))))

;;Calcula el n[umero total de frecuencias observadas
(define cantidad-n
  (lambda (listaX1X2-Fo)
    (apply +(map (lambda (x) (caddr x)) listaX1X2-Fo))))

;;Evalua un valor de la tabla discreta
(define ji-discreto-aux
  (lambda (distribucion fo x n)
    (estadistico fo (fe-discreta distribucion x n))))

;;Evalua un valor de la tabla continua
(define ji-continua-aux
  (lambda (distribucion fo x1 x2 n)
    (estadistico fo (fe-continua distribucion x1 x2 n))))

;;Crea la frecuencia esperada de las variables discretas
(define fe-discreta
  (lambda (distribucion x n)
    (frecuencia-esperada (distribucion x) n)))

;;Crea la frecuencia esperada de las continuas utilizando simpson
(define fe-continua
  (lambda (distribucion x1 x2 n)
    (frecuencia-esperada (integral distribucion x1 x2) n)))

;;Genera la frecuencia esperada a partir de la probabilidad y la cantidad de valores
(define frecuencia-esperada
  (lambda (probabilidad n)
    (* probabilidad n)))

;;Resuleve el estadistico a partir de las frecuencias
(define estadistico
  (lambda (fo fe)
         (/ (estadistico-aux fo fe) fe)))

;;Calcula parte del estadistico
(define estadistico-aux
  (lambda (fo fe)
    (expt (- fo fe) 2)))

;;Generador de frecuencias observadas para valores discretos y continuos enteros
(define generador-Fo
  (lambda (listaX cantidad discreta archivo)
    (cond ((eq? discreta #t)
           (define lista-discreta (generador-discreto listaX cantidad))
           (escribir-archivo lista-discreta archivo))
          (else (define lista-continua (generador-continuo listaX cantidad))
                (escribir-archivo lista-continua archivo)))))

;;Generador de variables discretas para una lista
(define generador-discreto
  (lambda (listaX cantidad)
    (cond ((eq? listaX '()) '())
          (else (append (generador-discreto-aux (car listaX) (car cantidad)) (generador-discreto (cdr listaX) (cdr cantidad)))))))

;;Generador de variables discretas a partir de un valor
(define generador-discreto-aux
  (lambda (valor cantidad)
    (cond ((eq? cantidad 0) '())
          (else (cons valor (generador-discreto-aux valor (- cantidad 1)))))))

;;Generador de valores continuos segun una lista
(define generador-continuo
  (lambda (listaX cantidad)
    (cond ((eq? listaX '()) '())
          (else (append (generador-continuo-aux (caar listaX) (cadar listaX) (car cantidad)) (generador-continuo (cdr listaX) (cdr cantidad)))))))

;;Genera variables aleatorias de numeros enteros
(define generador-continuo-aux
  (lambda (a b cantidad)
    (cond ((eq? cantidad 0) '())
          (else (cons (crear-random a b) (generador-continuo-aux a b (- cantidad 1)))))))

;;Genera un valor random de enteros
(define crear-random
  (lambda (a b)
    (define rand (random b))
    (cond ((eq? (pertenece a b rand) #t) rand)
         (else (crear-random a b)))))


;;Verificar si el valor se encuentra dentro del intervalo
(define pertenece
  (lambda (a b valor)
    (and (> valor a) (< valor b))))

;;Leer archivo
(define leer-archivo
  (lambda (archivo)
  (call-with-input-file archivo
  (lambda (p)
    (let f ((x (read p)))
      (if (eof-object? x)
          '()
          (cons x (f (read p)))))))))

;;Escribir archivo
(define escribir-archivo
  (lambda (lista archivo)
       (call-with-output-file archivo #:exists 'truncate
        (lambda (salida)
          (display lista salida)))))