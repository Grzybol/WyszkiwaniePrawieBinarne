import kotlinx.coroutines.*
import java.io.File
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

data class IndexedValue(val value: Int, val index: Int)

suspend fun parallelBinarySearch(arr: List<IndexedValue>, target: Int, numThreads: Int): List<Int> = coroutineScope {
    val segmentSize = (arr.size + numThreads - 1) / numThreads
    val jobs = mutableListOf<Deferred<List<Int>>>()

    for (i in 0 until numThreads) {
        val start = i * segmentSize
        val end = min(start + segmentSize, arr.size)
        jobs.add(async {
            binarySearch(arr, target, start, end)
        })
    }

    jobs.awaitAll().flatten().sorted()
}

fun binarySearch(arr: List<IndexedValue>, target: Int, start: Int, end: Int): List<Int> {
    val results = mutableListOf<Int>()
    var low = start
    var high = end - 1

    while (low <= high) {
        val mid = low + (high - low) / 2

        when {
            arr[mid].value == target -> {
                results.add(arr[mid].index)
                // Search both left and right of mid
                var left = mid - 1
                while (left >= low && arr[left].value == target) {
                    results.add(arr[left].index)
                    left--
                }
                var right = mid + 1
                while (right <= high && arr[right].value == target) {
                    results.add(arr[right].index)
                    right++
                }
                break
            }
            arr[mid].value < target -> low = mid + 1
            else -> high = mid - 1
        }
    }

    return results
}

fun createRandomFile(filePath: String, fileName: String, minLength: Int, start: Int = 0, end: Int = 100) {
    val file = File(filePath, fileName)
    file.bufferedWriter().use { out ->
        repeat(minLength) {
            out.write("${Random.nextInt(start, end)}\n")
        }
    }
    println("Plik został utworzony: ${file.absolutePath}")
}

fun main() = runBlocking {
    print("Czy chcesz utworzyć losowy plik? (tak/nie): ")
    val createFile = readLine()?.toLowerCase() == "tak"

    if (createFile) {
        print("Podaj ścieżkę do zapisu pliku: ")
        val filePath = readLine() ?: run {
            println("Nie podano ścieżki do zapisu pliku.")
            exitProcess(1)
        }

        print("Podaj nazwę pliku: ")
        val fileName = readLine() ?: run {
            println("Nie podano nazwy pliku.")
            exitProcess(1)
        }

        print("Podaj początek zakresu losowanych liczb: ")
        val start = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową wartość.")
            exitProcess(1)
        }

        print("Podaj koniec zakresu losowanych liczb: ")
        val end = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową wartość.")
            exitProcess(1)
        }

        print("Podaj minimalną długość pliku: ")
        val minLength = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową długość.")
            exitProcess(1)
        }

        createRandomFile(filePath, fileName, minLength, start, end)
    } else {
        print("Podaj ścieżkę do pliku: ")
        val filePath = readLine() ?: run {
            println("Nie podano ścieżki do pliku.")
            exitProcess(1)
        }

        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            println("Plik nie istnieje lub nie jest plikiem.")
            exitProcess(1)
        }

        //val arr = file.readLines().mapIndexedNotNull { index, line -> line.toIntOrNull()?.let { IndexedValue(it, index) } }.sortedBy { it.value }
        val arr = file.useLines { lines ->
            lines.mapIndexedNotNull { index, line -> line.toIntOrNull()?.let { IndexedValue(it, index) } }
                .sortedBy { it.value }
                .toList()
        }

        print("Co chcesz znaleźć: ")
        val target = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową wartość do wyszukania.")
            exitProcess(1)
        }

        print("Podaj liczbę wątków: ")
        val numThreads = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową liczbę wątków.")
            exitProcess(1)
        }

        val timeTaken = measureTimeMillis {
            val results = parallelBinarySearch(arr, target, numThreads)
            if (results.isNotEmpty()) {
                println("Elementy znalezione na indeksach: ${results.joinToString(", ")}")
            } else {
                println("Elementy nie znalezione")
            }
        }

        println("Czas wyszukiwania: $timeTaken ms")
    }
}