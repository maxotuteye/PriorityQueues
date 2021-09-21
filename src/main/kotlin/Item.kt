class Item(
    val severity: Int,
    var patientName: String
) : Comparable<Any?> {

    override fun compareTo(other: Any?): Int {
        val otherItem = other as Item
        return (severity - otherItem.severity) * -1
    }

    override fun equals(other: Any?): Boolean {
        return this.compareTo(other) == 0
    }

    override fun toString(): String {
        return "\nPatient: $patientName\nSeverity: $severity"
    }

    override fun hashCode(): Int {
        var result = severity
        result = 31 * result + patientName.hashCode()
        return result
    }
}