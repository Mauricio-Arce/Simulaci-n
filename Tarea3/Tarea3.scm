#lang scheme
;;Definiciones globales

(define e 2.718281828)
(define n-simpson 10)
(define pi 3.14159265359)
(define particion 1/10)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Distribuciones de Probabilidad
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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
  (lambda (es-discreta? cant distribucion inicio fin archivo)
    (cond ((eq? es-discreta? #t) (escribir-archivo (variables-discretas cant distribucion inicio fin) (string-append archivo "-d.txt")) #t)
          (else (escribir-archivo (variables-continuas cant distribucion inicio fin) (string-append archivo "-c.txt")) #t))));;)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Variables ALeatorias Discretas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Genera los randoms y la lista del rango de las variables discretas
(define variables-discretas
  (lambda (cantidad distribucion inicio fin)
    (define rango (cons inicio fin))
    (define randoms (generador-random cantidad))
    (define intervalo (intervalo-discreto distribucion inicio fin))
    (cons rango (cons intervalo randoms))))

;;Mapea la lista del rango con la distribucion respectiva
(define intervalo-discreto
  (lambda (distribucion inicio fin)
  (map (lambda (x) (distribucion x))  (lista-rango inicio fin))))

;;Lista con el rango de inicio a fin
(define lista-rango
  (lambda (inicio fin)
    (cond ((eq? inicio (+ fin 1)) '())
          (else (cons inicio (lista-rango (+ inicio 1) fin))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Variables Aleatorias Continuas
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Funcion para generar las variables aleatorias y los rangos con Simpson
(define variables-continuas
  (lambda (cantidad distribucion inicio fin)
    (define intervalo (mapeo-simpson inicio fin distribucion))
    (define randoms (calcular-random cantidad intervalo))
    (sort randoms <)))

;;Genera los n variables continuas
(define calcular-random
  (lambda (cantidad intervalo)
    (define random-actual (random))
    (cond ((eq? cantidad 0) '())
          (else (cons (random-continuo (caar (intervalo-asociado intervalo random-actual)) random-actual) (calcular-random (- cantidad 1) intervalo))))))

(define intervalo-asociado
  (lambda (intervalo random-actual)
    (filter (lambda (x) (< random-actual (cadr (car x)))) intervalo)))

;;Generador de random aleatorio
(define random-continuo
  (lambda (intervalo random)
    (define res (+ (first intervalo) (* (- (second intervalo) (first intervalo)) random)))
    res))

;;Evalua cada intervalo en simpson
(define mapeo-simpson
  (lambda (inicio fin distribucion)
    (define intervalo (intervalo-continuo inicio fin))
    (define resultados (sort (map (lambda (x) (simpson distribucion (car x) (cadr x))) intervalo) #:key second >))
    resultados))
    
;;Genera los intervalos de las variables continuas
(define intervalo-continuo
  (lambda (inicio fin)
    (cond ((eq? inicio fin) '())
          (else (cons (cons (/ inicio 1.0) (cons (+ inicio 0.01) '())) (intervalo-continuo (+ inicio particion) fin))))))

;;Funcion de Simpson
(define simpson
  (lambda (distribucion a b)
    (define valor-h (h a b n-simpson))
    (define resultado (* (/ valor-h 6) (+ (distribucion (Xi a valor-h a)) (simpson-sumatorias n-simpson distribucion a valor-h) (distribucion (Xi b valor-h b)))))
       (cons (cons a (cons b '())) (cons resultado '()))))

;;Crea una lista con solo valores impares
(define lista-impares
  (lambda (n distribucion h a)
    (cond ((eq? n 1) '(1))
          (else (cons (distribucion (Xi n h a)) (lista-impares (- n 2) distribucion h a))))))

;;Crea una lista con solo valores pares
(define lista-pares
  (lambda (n distribucion h a)
    (cond ((eq? n 0) '())
          (else (cons (distribucion (Xi n h a)) (lista-pares (- n 2) distribucion h a))))))

;;Genera las sumatorias de valores Xi pares y la sumatoria de Xi impares
(define simpson-sumatorias
  (lambda (n distribucion a h)
    (cond ((eq? (remainder n 2) 1) (n-impar n distribucion a h))
          (else (n-par n distribucion a h)))))

;;Crea las sumatorias de pares e impares cuando el n es impar
;;Retorna la suma de ambas expresiones
(define n-impar
  (lambda (n distribucion a h)
  (define impares (* 4 (apply +(lista-impares (- n 2) distribucion h a))))
  (define pares (* 2 (apply +(lista-pares (- n 1) distribucion h a))))
  (+ pares impares)))

;;Crea las sumatorias de pares e impares cuando el n es par
;;Retorna la suma de ambas expresiones
(define n-par
  (lambda (n distribucion a h)
    (define impares (* 4 (apply +(lista-impares (- n 1) distribucion h a))))
    (define pares (* 2 (apply +(lista-pares (- n 2) distribucion h a))))
    (+ pares impares)))

;;Funcion para calcular el h
(define h
  (lambda (a b n)
    (define valor-h (/ (- b a) n))
    valor-h))

;;Funcion para calcular los Xi de Simpson
(define Xi
  (lambda (i h a)
  (+ a (* i h))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;Funciones Extra
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;Genera los randoms
(define generador-random
  (lambda (n)
    (cond ((zero? n) '())
          (else (cons (random) (generador-random (- n 1)))))))

; Escribe un archivo con la lista 
(define escribir-archivo
  (lambda (lista archivo)
  ;; (existe-archivo archivo)
       (call-with-output-file archivo #:exists 'truncate
        (lambda (salida)
          (map (lambda (x)(display x salida)(newline salida)) lista )))lista))