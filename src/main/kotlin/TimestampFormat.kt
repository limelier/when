enum class TimestampFormat(val description: String, val char: Char?) {
    DEFAULT(
        "Default (Short Date/Time)",
        null
    ),
    SHORT_TIME(
        "Short Time (no seconds)",
        't'
    ),
    LONG_TIME(
        "Long Time (with seconds)",
        'T'
    ),
    SHORT_DATE(
        "Short Date",
        'd'
    ),
    LONG_DATE(
        "Long Date",
        'D'
    ),
    SHORT_DATE_TIME(
        "Short Date/Time",
        'f'
    ),
    LONG_DATE_TIME(
        "Long Date/Time (with day of week)",
        'F'
    ),
    RELATIVE_TIME(
        "Relative Time",
        'R'
    )
}