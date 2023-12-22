package pers.nmcma.fr

fun List<String>.toSaveFormatString(): String {
    val strBuilder=StringBuilder()
    for(it in this){
        strBuilder.append(it).append(',')
    }
    if (strBuilder.endsWith(',')) strBuilder.trimEnd(',')
    return strBuilder.toString()
}