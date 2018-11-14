package com.programmerare.crsTransformations.coordinate

import com.programmerare.crsTransformations.crsIdentifier.CrsIdentifier

/**
 * An instance of the coordinate aggregates three values:
 * - X / Easting / Longitude  
 * - Y / Northing / Latitude  
 * - CRS (Coordinate Reference System) identifier with the EPSG code which defines the coordinate system  
 * 
 * There are multiple accessors for each of the "x" and "y" values as described below.  
 * Depending on the desired semantic in your context, you may want to use the different accessors like this:  
 *      - x/y for a geocentric or cartesian system  
 *      - longitude/latitude for a geodetic or geographic system  
 *      - easting/northing for a cartographic or projected system  
 *      - xEastingLongitude/yNorthingLatitude for general code handling different types of system
 *
 * @author Tomas Johansson ( http://programmerare.com )
 * The code in the "crs-transformation-adapter-core" project is licensed with MIT.
 * Other subprojects may be released with other licenses e.g. LGPL or Apache License 2.0.
 * Please find more information in the license file at the root directory of each subproject
 * (e.g. the subprojects "crs-transformation-adapter-impl-geotools" , "crs-transformation-adapter-impl-proj4j" and so on)
*/
class CrsCoordinate private constructor(
    /**
     * One of the four accessors for the part of the coordinate that represents the east-west/X/Longitude direction. 
     */
    val xEastingLongitude: Double,

    /**
     * One of the four accessors for the part of the coordinate that represents the east-west/Y/Latitude direction.
     */    
    val yNorthingLatitude: Double,

    /**
     * CRS (Coordinate Reference System) identifier with the EPSG code 
     * which defines the coordinate reference system for the coordinate instance. 
     */    
    val crsIdentifier: CrsIdentifier
) {
    // The constructor is private to force client code to use the below factory methods
    // which are named to indicate the order of the longitude and latitude parameters.

    // -----------------------
    // Three getters for X / Easting / Longitude :

    /**
     * One of the four accessors for the part of the coordinate that represents the east-west direction.
     * "X" is typically used for a geocentric or cartesian coordinate reference system.
     */
    fun getX(): Double {
        return xEastingLongitude
    }

    /**
     * One of the four accessors for the part of the coordinate that represents the east-west direction.
     * "Easting" is typically used for a cartographic or projected coordinate reference system.
     */    
    fun getEasting(): Double {
        return xEastingLongitude
    }

    /**
     * One of the four accessors for the part of the coordinate that represents the east-west direction.
     * "Longitude" is typically used for a geodetic or geographic coordinate reference system.
     */    
    fun getLongitude(): Double {
        return xEastingLongitude
    }
    // -----------------------
    // Three getters for Y / Northing / Latitude :

    /**
     * One of the four accessors for the part of the coordinate that represents the north-south direction.
     * "Y" is typically used for a geocentric or cartesian coordinate reference system.
     */    
    fun getY(): Double {
        return yNorthingLatitude
    }

    /**
     * One of the four accessors for the part of the coordinate that represents the north-south direction.
     * "Northing" is typically used for a cartographic or projected coordinate reference system.
     */    
    fun getNorthing(): Double {
        return yNorthingLatitude
    }

    /**
     * One of the four accessors for the part of the coordinate that represents the north-south direction.
     * "Latitude" is typically used for a geodetic or geographic coordinate reference system.
     */    
    fun getLatitude(): Double {
        return yNorthingLatitude
    }
    //------------------------------------------------------------------
    // The implementation of the following three methods were generated by IntelliJ IDEA

    /**
     * Implementation generated by IntelliJ IDEA.
     */    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CrsCoordinate

        if (xEastingLongitude != other.xEastingLongitude) return false
        if (yNorthingLatitude != other.yNorthingLatitude) return false
        if (crsIdentifier != other.crsIdentifier) return false

        return true
    }

    /**
     * Implementation generated by IntelliJ IDEA.
     */    
    override fun hashCode(): Int {
        var result = xEastingLongitude.hashCode()
        result = 31 * result + yNorthingLatitude.hashCode()
        result = 31 * result + crsIdentifier.hashCode()
        return result
    }

    /**
     * Implementation generated by IntelliJ IDEA.
     */    
    override fun toString(): String {
        return "Coordinate(xEastingLongitude=$xEastingLongitude, yNorthingLatitude=$yNorthingLatitude, crsIdentifier=$crsIdentifier)"
    }
    //------------------------------------------------------------------

    internal companion object {
        /**
         * Kotlin factory method with access level "internal".
         * NOT indended for public use.
         * Please instead use the Java factory class CrsCoordinateFactory 
         * or the package level factory methods in the coordinate package. 
         */
        @JvmStatic        
        internal fun _internalXYfactory(
            xEastingLongitude: Double,
            yNorthingLatitude: Double,
            crsIdentifier: CrsIdentifier
        ): CrsCoordinate {
            return CrsCoordinate( // private constructor
                xEastingLongitude,
                yNorthingLatitude,
                crsIdentifier
            )
        }    
    }
}