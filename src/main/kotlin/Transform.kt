const val hour = "(?<hour>[01]?[0-9]|2[0-3])"
const val minute = "(?<minute>[0-5][0-9])"
const val amPm = " ?(?<ampm>[aApP][mM])"
val timeRegex = Regex("""\b${hour}(:${minute})?(${amPm})?\b""")

fun String.timeTransform(): String {
    return this.replace(timeRegex, "<time>")
}