class CustomPriorityQueue<E> {
    var myArr = ArrayList<E?>()
    var step = 1

    fun add(item: E, ordered: Boolean) {
        myArr.add(item)

//        fun orderQueue() {
//            var tempIndex = myArr.size - 1
//            if (myArr.size != 2) {
//                while (tempIndex != 1
//                    && ((myArr[tempIndex] as Item).compareTo(myArr[tempIndex / 2])) <= 0) {
//                    //println("tempIndex: $tempIndex \tmyArr[tempIndex]: ${myArr[tempIndex]}")
//                    myArr[tempIndex] =
//                        myArr.set(tempIndex / 2, item) // swapping objects at index tempIndex and tempIndex / 2
//                    // println("myArr[tempIndex]: ${myArr[tempIndex]} \t item = $item")
//                    tempIndex /= 2
//                }
//            }
//        }
        //TODO: Modify function to show sorting in steps

//        orderQueue()
//        println("After ordering:")
//        for(data in myArr) println(data)
//        var tempIndex = myArr.size - 1
//        if (myArr.size != 2) {
//            while (tempIndex != 1 && ((myArr[tempIndex])!! != myArr[tempIndex / 2])) {
//                println("tempIndex: $tempIndex \tmyArr[tempIndex]: ${myArr[tempIndex]}")
//                myArr[tempIndex] =
//                    myArr.set(tempIndex / 2, item) // swapping objects at index tempIndex and tempIndex / 2
//                println("myArr[tempIndex]: ${myArr[tempIndex]} \t item = $item")
//                tempIndex /= 2
//            }
//        }
        //orderQueue(item)          // Prioritize new item
        step = 1
        if (ordered) orderQueue()
        for (data in myArr) println(data)
    }

    private fun orderQueue() {
        var item: E? = myArr[myArr.lastIndex]
        if ((item as Item).severity == -1) {
            myArr.removeAt(myArr.lastIndex)
            println("!!!!!!!!!!!!!!!!!!!!x: ${item.patientName}")
        }

        item = myArr[myArr.lastIndex]
        var tempIndex = myArr.size - 1
        if (myArr.size != 2) {
            while (tempIndex != 1
                && ((myArr[tempIndex] as Item).compareTo(myArr[tempIndex / 2])) <= 0
                && step == 1
            ) {
                println("tempIndex: $tempIndex \tmyArr[tempIndex]: ${myArr[tempIndex]}")
                myArr[tempIndex] =
                    myArr.set(tempIndex / 2, item) // swapping objects at index tempIndex and tempIndex / 2
                // println("myArr[tempIndex]: ${myArr[tempIndex]} \t item = $item")
                tempIndex /= 2
                step = 1
            }
        }
    }

    fun remove(): E? {
        if (myArr.size == 1) throw NoSuchElementException("The priority queue is empty.")
        var tempIndex = 1 // index where the item added from the bottom of the queue to the top currently is
        val removedItem = myArr[1]
        myArr[1] = myArr[myArr.size - 1]
        myArr.removeAt(myArr.size - 1)
        var cont = true
        while (!isEmpty && (isLeftNotNull(tempIndex) || isRightNotNull(tempIndex)) && cont)
        // loop runs until both left and right are null.
        // there will be iterations where no switches are made if item is smaller than both left and right
        {
            if (isRightNotNull(tempIndex)) // if both are not null
            {
                var min = 0 // min represents left or right being the smaller value. 0 is left, 1 is right
                if ((myArr[tempIndex * 2 + 1] as Item) < myArr[tempIndex * 2]) min =
                    1

                //here, min is the smaller of the left/right values.
                if ((myArr[tempIndex * 2 + min] as Item) < myArr[tempIndex]) // if the min of left and right is smaller than the item, then switch them.
                {
                    myArr[tempIndex] = myArr.set(tempIndex * 2 + min, myArr[tempIndex])
                    tempIndex = tempIndex * 2 + min
                } else cont = false
            } else  // if only left is not null.
            {
                if ((myArr[tempIndex * 2] as Item) < myArr[tempIndex]) // if left is smaller than item, then switch
                {
                    myArr[tempIndex] = myArr.set(tempIndex * 2, myArr[tempIndex]) //switching left and item
                    tempIndex *= 2 // updating tempIndex to follow the item
                } else cont = false
            }
        } // at end of loop, all necessary switches are made, item is in correct position.
        return removedItem
    }

    val isEmpty: Boolean
        get() = myArr.size == 1

    fun peek(): E? {
        return myArr[1]
    }

    fun size(): Int {
        return myArr.size
    }

    private fun isLeftNotNull(index: Int): Boolean {
        try {
            if (myArr[index * 2] == null) return false
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
        return true
    }

    private fun isRightNotNull(index: Int): Boolean {
        try {
            if (myArr[index * 2 + 1] == null) return false
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
        return true
    }

    val arrayList: ArrayList<*>
        get() = myArr

    init {
        myArr.add(null)
    }
}