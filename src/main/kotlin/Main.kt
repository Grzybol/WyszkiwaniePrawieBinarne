import kotlinx.coroutines.*
import java.io.File
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.exitProcess

suspend fun parallelAlmostBinarySearch(arr: IntArray, target: Int, numThreads: Int): Int? = coroutineScope {
    val segmentSize = (arr.size + numThreads - 1) / numThreads
    val jobs = mutableListOf<Deferred<Int?>>()

    for (i in 0 until numThreads) {
        val start = i * segmentSize
        val end = min(start + segmentSize, arr.size)
        jobs.add(async {
            almostBinarySearch(arr, target, start, end)
        })
    }

    jobs.awaitAll().firstOrNull { it != null }
}

fun almostBinarySearch(arr: IntArray, target: Int, start: Int, end: Int): Int? {
    var low = start
    var high = end - 1

    while (low <= high) {
        val mid = low + (high - low) / 2

        when {
            arr[mid] == target -> return mid
            arr[mid] < target -> low = mid + 1
            else -> high = mid - 1
        }
    }

    return null
}

fun createRandomFile(filePath: String, fileName: String, minLength: Int) {
    val file = File(filePath, fileName)
    file.bufferedWriter().use { out ->
        repeat(minLength) {
            out.write("${Random.nextInt(0, 1000)}\n")
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

        print("Podaj minimalną długość pliku (ilość linii): ")
        val minLength = readLine()?.toIntOrNull() ?: run {
            println("Podano nieprawidłową długość.")
            exitProcess(1)
        }

        createRandomFile(filePath, fileName, minLength)
    } else {
        print("Podaj ścieżkę do pliku: ")
        val filePath = readLine() ?: run {
            println("Nie podano ścieżki do pliku.")
            exitProcess(1)
        }

        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            println("Plik nie istnieje.")
            exitProcess(1)
        }

        val arr = file.readLines().mapNotNull { it.toIntOrNull() }.toIntArray()

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

        val result = parallelAlmostBinarySearch(arr, target, numThreads)
        if (result != null) {
            println("Element znaleziony na indeksie: $result")
        } else {
            println("Element nie znaleziony")
        }
    }
}