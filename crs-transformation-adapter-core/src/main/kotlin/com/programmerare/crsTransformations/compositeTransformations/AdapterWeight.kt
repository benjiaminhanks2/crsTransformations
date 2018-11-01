package com.programmerare.crsTransformations.compositeTransformations

import com.programmerare.crsTransformations.CrsTransformationAdapter
import com.programmerare.crsTransformations.CrsTransformationAdapterLeafFactory

class AdapterWeight private constructor(
        val crsTransformationAdapter: CrsTransformationAdapter,
        val weight: Double
) {

    /**
     * Not intended to be used with ".Companion" from client code.
     * The reason for its existence has to do with the fact that the
     * JVM class has been created with the programming language Kotlin.
     */
    companion object {

        /**
         * @param crsTransformationAdapterClassName the full class name (i.e. including the package name)
         *  of a class which must implement the interface CrsTransformationAdapter
         */
        @JvmStatic
        fun createFromStringWithFullClassNameForImplementation(
            crsTransformationAdapterClassName: String,
            weight: Double
        ): AdapterWeight {
            val crsTransformationAdapter = CrsTransformationAdapterLeafFactory.createCrsTransformationAdapter(crsTransformationAdapterClassName)
            return AdapterWeight(crsTransformationAdapter, weight)
        }

        @JvmStatic
        fun createFromInstance(
                crsTransformationAdapter: CrsTransformationAdapter,
                weight: Double
        ): AdapterWeight{
            return AdapterWeight(crsTransformationAdapter, weight)
        }
    }
}