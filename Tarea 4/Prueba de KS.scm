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

;;(define iota
;;  (lambda (n)
;;    (iota-aux n '())))

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



;(acumulada ((pruebaKS #t 1) (lambda (x) (/ 1 6))  '(4 3 6  4 6  2 4 3 2 4 5 6 3 2 4 4 5 3 2 1  1 1 3 3 4 3 2)))
;; (apply max (map (lambda (lis) (abs (- (cadr lis) (caddr lis))))(acumulada ((pruebaKS #t 1) (lambda (x) (/ 1 6))  '(4 3 6  4 6  2 4 3 2 4 5 6 3 2 4 4 5 3 2 1  1 1 3 3 4 3 2)))))  

(define tabla-prob-obs-discreta
  (lambda (muestra)
    (map (lambda (l) (list (car l) (/ (length l) (length muestra)))) (agrupar (sort muestra <)))))

(define tabla-prob-obs-continua-intervalo
  (lambda (intervalo)
    (lambda (muestra)
      (conteo (sort muestra <) intervalo))))

(define tabla-prob-obs-continua
  (tabla-prob-obs-continua-intervalo 0.2))

(define conteo
  (lambda (muestra-ordenada intervalo)
    (conteo-aux muestra-ordenada
                (list (car muestra-ordenada)
                      (+ (car muestra-ordenada) intervalo)
                      0)
                intervalo)))

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

(define agrupar
  (lambda (l)
    (cond ((null? l) l)
          (else (agrupar-aux (cdr l) (list (car l)))))))

(define agrupar-aux
  (lambda (l grupo)
    (cond ((null? l) (list grupo))
          ((equal? (car l) (car grupo))
           (agrupar-aux (cdr l) (cons (car grupo) grupo)))
          (else
           (cons grupo (agrupar-aux (cdr l) (list (car l)))
                 )))))


;Funciona para discretas y continuas por igual... (mientras el formato esté bien)
(define acumulada
  (lambda (tabla-ks)
    (acumulada-aux (cdr tabla-ks) (list (car tabla-ks)) (cadar tabla-ks) (caddar tabla-ks))))

(define acumulada-aux
  (lambda (tabla acum poa poe)
    (cond ((null? tabla) (reverse acum))
          (else
           (acumulada-aux
            (cdr tabla)
            (cons (list (caar tabla)
                        (+ (cadar tabla) poa)
                        (+ (caddar tabla) poe))
                  acum)
            (+ (cadar tabla) poa)
            (+ (caddar tabla) poe))))))
  

(define pruebaKS
  (lambda (es-discreta intervalo)
    (lambda (fun muestra)
      (cond (es-discreta (ks-aux (map (lambda (l) (list (car l) (cadr l) (fun (car l))))
                                      (tabla-prob-obs-discreta muestra))))
            (else (ks-aux (map (lambda (l) (list (car l) (cadr l) (integral fun (caar l) (cadar l))))
                               (tabla-prob-obs-continua muestra))))))))

(define ks-aux
  (lambda (tabla-casi-ks)
    (apply max
           (map (lambda (lis) (abs (- (cadr lis) (caddr lis))))
                (acumulada tabla-casi-ks)))))
                          