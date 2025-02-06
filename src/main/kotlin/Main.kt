import kotlinx.coroutines.*
import kotlin.math.min

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

fun main() = runBlocking {
    val arr = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val target = 7
    val numThreads = 4

    val result = parallelAlmostBinarySearch(arr, target, numThreads)
    if (result != null) {
        println("Element found at index: $result")
    } else {
        println("Element not found")
    }
}