
# Dane przekazywane do funkcji pogrupowane beda w nastepujace struktury.

# Zmienna rynek opisuje parametry aktywa i rynku. Jest to lista zawierajaca pola:
# r - (roczna) stopa procentowa pozbawiona ryzyka
# t - chwila w ktorej rozpatrujemy stan rynku / wyceniamy opcje
# S - cena spot w chwili t
# vol - zmiennosc (roczna) aktywa
# mu - dryft (roczny) aktywa

# Zmienna opcja opisuje parametry wycenianej opcji. Jest to lista zawierajaca pola:
# E - wartosc strike
# T - czas do wygasniecia opcji
# type - typ opcji, CALL lub PUT

#Stale oznaczajace typ opcji
CALL <- 0
PUT <- 1

################################################################################
####### Funkcje obliczajace cene i delte opcji waniliowych ze wzoru BS #########
################################################################################

# Intrisnic value waniliowej opcji europejskiej.
# S - kurs aktywa
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca IV
intrisnicValue <- function(S, opcja)
{
  if (opcja$type == CALL) return( max(0, S - opcja$E) )
  else return( max(0, opcja$E - S) )
}

# Cena opcji europejskiej liczona ze wzoru Blacka-Scholesa.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca cene europejskiej opcji
price <- function(rynek, opcja)
{
  if (opcja$type == CALL) return( priceCall(rynek, opcja) )
  else return( pricePut(rynek, opcja) )
}

# Wartosc d1 wystepujaca we wzorach na cene opcji.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca wartosc d1
d1 <- function(rynek, opcja)
{
  (log(rynek$S / opcja$E) + 
     (rynek$r - rynek$q + rynek$vol^2/2)*(opcja$T - rynek$t) ) /
    (rynek$vol*sqrt(opcja$T - rynek$t))
}

# Cena europejskiej opcji call liczona ze wzoru Blacka-Scholesa
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca cene europejskiej opcji call
priceCall <- function(rynek, opcja)
{
  #dziala nawet gdy K=0!
  d1_ <- d1(rynek, opcja)
  d2_ <- d1_ - rynek$vol * sqrt(opcja$T - rynek$t)
  rynek$S * pnorm(d1_) * exp(-rynek$q * (opcja$T - rynek$t)) -
    opcja$E * pnorm(d2_) * exp(-rynek$r * (opcja$T - rynek$t))
}

# Cena europejskiej opcji put liczona ze wzoru Blacka-Scholesa
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca cene europejskiej opcji put
pricePut <- function(rynek, opcja)
{
  d1_ <- d1(rynek, opcja)
  d2_ <- d1_ - rynek$vol * sqrt(opcja$T - rynek$t)
  opcja$E * pnorm(-d2_) * exp(-rynek$r * (opcja$T - rynek$t)) - 
    rynek$S * pnorm(-d1_) * exp(-rynek$q * (opcja$T - rynek$t))
}

# Delta opcji europejskiej.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca delte opcji europejskiej.
delta <- function(rynek, opcja)
{
  if (opcja$type == CALL) return( deltaCall(rynek, opcja) )
  else return( deltaPut(rynek, opcja) )
}

# Delta europejskiej opcji call.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca delte europejskiej opcji call.
deltaCall <- function(rynek, opcja)
{
  pnorm( d1(rynek, opcja) )
}

# Delta europejskiej opcji put.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# opcja - lista z danymi opcji, opisana na poczatku tego pliku
# return - liczba oznaczajaca delte europejskiej opcji put
deltaPut <- function(rynek, opcja)
{
  pnorm( d1(rynek, opcja) ) - 1
}

################################################################################
################### Generowanie trajektorii ####################################
################################################################################

# Generuje trajektorie aktywa. 
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# T - koncowa chwila na trajektorii
# K - w ilu punkach wygenerowac cene
# return - wektor dlugosci K+1 z cenami aktywa w chwilach 0, T/K, ..., T
trajectory <- function(rynek, T, K)
{
  dt <- (T - rynek$t)/K
  mean_ <- (rynek$r - rynek$vol * rynek$vol/2) * dt
  logs <- c( log(rynek$S),
             rnorm(K, mean=mean_, sd=rynek$vol*sqrt(dt)) )
  logs <- cumsum(logs)
  exp(logs)
}

trajectoryAnti <- function(rynek, T, K)
{
  dt <- (T - rynek$t)/K
  mean_ <- (rynek$r - rynek$vol * rynek$vol/2) * dt
  N <- rnorm(K, sd=rynek$vol*sqrt(dt))
  logs_pos <- c( log(rynek$S), (mean_ + N) )
  logs_neg <- c( log(rynek$S), (mean_ - N) )
  logs_pos <- cumsum(logs_pos)
  logs_neg <- cumsum(logs_neg)
  list( pos = exp(logs_pos),
        neg = exp(logs_neg)
  )
}

################################################################################
###### Funkcje pomocniczne do obliczania ceny opcji metoda Monte Carlo #########
################################################################################

# Tworzy funkcje przekazywana estymatorowi AV.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# T - koncowa chwila na trajektorii
# K - w ilu punkach wygenerowac cene
# payoff - funkcja przyjmujaca trajektorie i zwracajaca jako wynik wyplate z instrumentu
# return - funkcja przyjmujaca liczbe n, zwracajaca wektor dlugosci n oznaczajacy
#    wyplaty z instrumenu w kolejnych replikacjach
funForCMC <- function(rynek, T, K, payoff)
{
  function(n)
  {
    sapply(1:n, function(foo) payoff(trajectory(rynek, T, K)))
  }
}

# Tworzy funkcje przekazywana estymatorowi AV.
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# T - koncowa chwila na trajektorii
# K - w ilu punkach wygenerowac cene
# payoff - funkcja przyjmujaca trajektorie i zwracajaca jako wynik wyplate z instrumentu
# return - funkcja przyjmujaca liczbe n, zwracajaca liste z polami fst i snd oznaczajacymi
#    wektory wyplat na sciezkach (fst) i sciezkach antyteteycznych (snd)
funForAV <- function(rynek, T, K, payoff)
{
  function(n)
  {
    res <- sapply(1:n, function(foo) {
      tr <- trajectoryAnti(rynek, T, K)
      c( payoff(tr$pos), payoff(tr$neg) )
    })
    list( fst = res[1,], snd = res[2,] )
  }
}

# Tworzy funkcje przekazywana estymatorowi CV. Jako zmienna kontrola jest brana
# wartosc trajektorii na koncu sciezki
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# T - koncowa chwila na trajektorii
# K - w ilu punkach wygenerowac cene
# payoff - funkcja przyjmujaca trajektorie i zwracajaca jako wynik wyplate z instrumentu
# return - funkcja przyjmujaca liczbe n, zwracajaca liste z polami fst i snd oznaczajacymi
#    wektory wyplat na sciezkach (fst) i sciezkach antyteteycznych (snd)
funForCV <- function(rynek, T, K, payoff)
{
  function(n)
  {
    res <- sapply(1:n, function(foo) {
      tr <- trajectory(rynek, T, K)
      c( payoff(tr), tr[K+1] )
    })
    list( Y = res[1,], X = res[2,] )
  }
}

# Tworzy funkcje przekazywana estymatorowi CV. Jako zmienna kontrola jest brana
# wyplata z opcji przy wykonaniu bez bariery (wartoscia oczekiwana jest wtedy
# zakumulowana cena BS).
# rynek - lista z danymi rynku, opisana na poczatku tego pliku
# T - koncowa chwila na trajektorii
# K - w ilu punkach wygenerowac cene
# payoff - funkcja przyjmujaca trajektorie i zwracajaca jako wynik wyplate z instrumentu
# return - funkcja przyjmujaca liczbe n, zwracajaca liste z polami fst i snd oznaczajacymi
#    wektory wyplat na sciezkach (fst) i sciezkach antyteteycznych (snd)
funForCV_barrier <- function(rynek, opcja, K, payoff)
{
  function(n)
  {
    res <- sapply(1:n, function(foo) {
      tr <- trajectory(rynek, opcja$T, K)
      c( payoff(tr), payoffVanilla(rynek$r, opcja)(tr) )
    })
    list( Y = res[1,], X = res[2,] )
  }
}
################################################################################
############# Funkcje obliczajace wyplaty opcji europejskich ###################
################################################################################

payoffVanilla <- function(r, option)
{
  function(tr)
  {
    S <- tr[length(tr)]
    exp(-r*option$T) * intrisnicValue(S, option)
  }
}

#Stale oznaczajace typ opcji
UP_AND_OUT   <- 0
UP_AND_IN    <- 1
DOWN_AND_OUT <- 2
DOWN_AND_IN  <- 3

optionActive <-function(tr, type, barrier)
{
  if (type == UP_AND_IN) return(max(tr) > barrier)
  else if (type == UP_AND_OUT) return(max(tr) < barrier)
  else if (type == DOWN_AND_OUT) return(min(tr) > barrier)
  else if (type == DOWN_AND_IN) return(min(tr) < barrier)
  else return(FALSE)
}

payoffBarrier <- function(r, option)
{
  function(tr)
  {
    S <- tr[length(tr)]
    if (optionActive(tr, option$barrierType, option$barrierLevel))
      return (exp(-r*T) * intrisnicValue(S, option))
    else return(0)
  }  
}

################################################################################
###### Funkcje obliczajace ceny opcji europejskich metoda Monte Carlo ##########

source("MonteCarlo.R")

priceCMC <- function(n, rynek, T, K, payoff)
{
  fun <- funForCMC(rynek, T, K, payoff)
  crudeMC(n, fun)
}

priceAV <- function(n, rynek, T, K, payoff)
{
  fun <- funForAV(rynek, T, K, payoff)
  antitheticVariates(n, fun)
}

priceCV <- function(n, rynek, T, K, payoff, CV_EX)
{
  fun <- funForCV(rynek, T, K, payoff)
  controlVariates(n, fun, CV_EX)
}

priceCV_barrier <- function(n, rynek, opcja, K, payoff)
{
  fun <- funForCV_barrier(rynek, opcja, K, payoff)
  controlVariates(n, fun, price(rynek, opcja))
}
