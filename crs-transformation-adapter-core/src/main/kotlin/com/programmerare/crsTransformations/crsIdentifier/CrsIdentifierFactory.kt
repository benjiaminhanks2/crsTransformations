/**
 * From Java code this will look like a class 'CrsIdentifierFactory'
 * with public static factory methods.
 * The Java class name: com.programmerare.crsTransformations.crsIdentifier.CrsIdentifierFactory  
 * 
 * From Kotlin code the methods are available as package level functions
 * and each function can be imported as if it would be a class, for example like this:  
 *  import com.programmerare.crsTransformations.crsIdentifier.createFromEpsgNumber
 */
@file:JvmName("CrsIdentifierFactory")
package com.programmerare.crsTransformations.crsIdentifier
// The reason for having CrsIdentifier and this CrsIdentifierFactory
// in a package of its own is to avoid "polluting" the base
// package from lots of package level function defined in this file
// when using Kotlin code.
// (when using Java we do not see that problem but rather a class
//   CrsIdentifierFactory with all these function as static method in that class)

private const val EPSG_PREFIX_UPPERCASED = "EPSG:"
private const val LENGTH_OF_EPSG_PREFIX = EPSG_PREFIX_UPPERCASED.length

// The crsCode string will become trimmed, and if it is
// "epsg" (or e.g. something like "ePsG") then it will be uppercased i.e. "EPSG"

/**
 * @param crsCode a string which should begin with "EPSG:4326" 
 *  (although it is case insensitive and it is also acceptable 
 *   with leading white spaces and e.g. " ePsG:4326" but 
 *   it will then be internally canonicalized to "EPSG:4326")  
 */
fun createFromCrsCode(crsCode: String): CrsIdentifier {
    // these two default values will be used, unless the crsCode parameter is an EPSG string
    var epsgNumber = 0
    var isEpsgCode = false

    if(crsCode.isNullOrBlank()) {
        throw java.lang.IllegalArgumentException("CRS code must be non-empty")
    }
    var crsIdentifierCode = crsCode.trim() // but does not uppercase here (unless EPSG below)

    if(crsIdentifierCode.toUpperCase().startsWith(EPSG_PREFIX_UPPERCASED)) {
        val nonEpsgPartOfString = crsIdentifierCode.substring(LENGTH_OF_EPSG_PREFIX);
        val epsgNumberOrNull = nonEpsgPartOfString.toIntOrNull()
        if(epsgNumberOrNull != null) {
            epsgNumber = epsgNumberOrNull
            isEpsgCode = true
            crsIdentifierCode = crsIdentifierCode.toUpperCase()
        }
    }
    return CrsIdentifier._internalCrsFactory(crsIdentifierCode, isEpsgCode, epsgNumber)
}

/**
 * @param epsgNumber an EPSG number, 
 *      for example 4326 for the frequently used coordinate reference system WGS84. 
 */
fun createFromEpsgNumber(epsgNumber: Int): CrsIdentifier {
    return CrsIdentifier._internalCrsFactory(
        crsCode = EPSG_PREFIX_UPPERCASED + epsgNumber,
        isEpsgCode = true,
        epsgNumber = epsgNumber
    )
}