# Wyszukiwanie Prawie Binarne

Ten projekt implementuje równoległe wyszukiwanie prawie binarne w języku Kotlin. Program może wyszukiwać elementy w tablicy liczb całkowitych, korzystając z zadanej liczby wątków. Dodatkowo, program umożliwia tworzenie losowych plików z liczbami całkowitymi, które następnie można analizować.

## Funkcje

- **Równoległe wyszukiwanie binarne**: Program dzieli tablicę na segmenty i przeszukuje je równolegle za pomocą korutyn. Wyniki są zbierane i zwracane jako lista indeksów.
- **Tworzenie losowych plików**: Program umożliwia tworzenie plików z losowymi liczbami całkowitymi o zadanej minimalnej długości.
- **Pomiar czasu wyszukiwania**: Program mierzy czas potrzebny na wykonanie wyszukiwania i wyświetla go w milisekundach.

## Implementowane podejście

### Równoległe Wyszukiwanie Binarne

Wyszukiwanie binarne jest efektywną metodą znajdowania elementu w posortowanej tablicy poprzez powtarzalne dzielenie jej na połowy. W tym projekcie implementujemy równoległe wyszukiwanie binarne, które dzieli tablicę na segmenty i przeszukuje je równolegle za pomocą korutyn. W ten sposób możemy przyspieszyć wyszukiwanie w dużych tablicach, wykorzystując wielowątkowość.

#### Kroki implementacji:

1. **Podział tablicy na segmenty**:
   - Tablica jest dzielona na segmenty w zależności od liczby zadanych wątków (`numThreads`). Każdy segment jest przeszukiwany równolegle.

2. **Równoległe przeszukiwanie segmentów**:
   - Każdy segment jest przeszukiwany za pomocą funkcji `binarySearch`, która wykonuje klasyczne binarne wyszukiwanie na zadanym zakresie indeksów.

3. **Zbieranie wyników**:
   - Wyniki z poszczególnych segmentów są zbierane i łączone. Indeksy znalezionych elementów są sortowane i zwracane jako wynik końcowy.

### Zalety:

- **Efektywność**: Równoległe wyszukiwanie binarne pozwala na szybsze przeszukiwanie dużych tablic dzięki wykorzystaniu wielu wątków.
- **Skalowalność**: Program można dostosować do różnych rozmiarów tablic i liczby dostępnych rdzeni procesora, zwiększając liczbę wątków.

## Sposób użycia

### Uruchomienie programu

1. Sklonuj repozytorium na swoje lokalne urządzenie:
    ```sh
    git clone <URL_REPOZYTORIUM>
    cd WyszukiwaniePrawieBinarne
    ```

2. Uruchom program:
    ```sh
    kotlinc ParallelBinarySearch.kt -include-runtime -d ParallelBinarySearch.jar
    java -Xms2g -Xmx4g -jar ParallelBinarySearch.jar
    ```

### Przykład użycia

1. **Tworzenie losowego pliku**:
    - Program zapyta, czy chcesz utworzyć losowy plik. Odpowiedz `tak`.
    - Podaj ścieżkę do zapisu pliku.
    - Podaj nazwę pliku.
    - Podaj początek zakresu losowanych liczb
    - Podaj koniec zakresu losowanych liczb
    - Podaj minimalną długość pliku.

2. **Analiza istniejącego pliku**:
    - Jeśli nie chcesz tworzyć losowego pliku, odpowiedz `nie`.
    - Podaj ścieżkę do istniejącego pliku.
    - Podaj wartość do wyszukania.
    - Podaj liczbę wątków do użycia.

### Przykładowy wynik

```plaintext
Czy chcesz utworzyć losowy plik? (tak/nie): nie
Podaj ścieżkę do pliku: D:\oblicznia_rownolegle\testfile
Co chcesz znaleźć: 99
Podaj liczbę wątków: 8
Elementy znalezione na indeksach: 15, 121, 215, 229,
Czas wyszukiwania: 42 ms
