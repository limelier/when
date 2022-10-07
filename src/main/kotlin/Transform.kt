fun String.dummyTransform(): String {
    return this.replace("\\d".toRegex(), "#")
}