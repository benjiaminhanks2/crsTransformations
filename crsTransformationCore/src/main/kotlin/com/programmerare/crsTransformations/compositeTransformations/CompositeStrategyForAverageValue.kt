package com.programmerare.crsTransformations.compositeTransformations

import com.programmerare.crsTransformations.*

class CompositeStrategyForAverageValue(private val crsTransformationFacades: List<CrsTransformationFacade>) : CompositeStrategy {
    override fun getAllTransformationFacadesInTheOrderTheyShouldBeInvoked(): List<CrsTransformationFacade> {
        return crsTransformationFacades
    }

    override fun shouldInvokeNextFacade(allResultsSoFar: List<TransformResult>, lastResultOrNullIfNoPrevious: TransformResult?, nextFacadeToInvoke: CrsTransformationFacade): Boolean {
        return true
    }

    override fun shouldContinueIterationOfFacadesToInvoke(allResultsSoFar: List<TransformResult>, lastResultOrNullIfNoPrevious: TransformResult?): Boolean {
        return true
    }

    override fun calculateAggregatedResult(
            allResults: List<TransformResult>,
            inputCoordinate: Coordinate,
            crsIdentifierForOutputCoordinateSystem: CrsIdentifier
    ): TransformResult {
        var successCount = 0
        var sumLat = 0.0
        var sumLon = 0.0
        for (res: TransformResult in allResults) {
            if(res.isSuccess) {
                successCount++
                val coord = res.outputCoordinate
                sumLat += coord.yLatitude
                sumLon += coord.xLongitude
            }
        }
        if(successCount > 0) {
            var avgLat = sumLat / successCount
            var avgLon = sumLon / successCount
            val coordRes = Coordinate.createFromYLatXLong(avgLat, avgLon, crsIdentifierForOutputCoordinateSystem)
            return TransformResultImplementation(inputCoordinate, outputCoordinate = coordRes, exception = null, isSuccess = true)
        }
        else {
            // TODO: aggregate mroe from the results e.g. exception messages
            return TransformResultImplementation(inputCoordinate, outputCoordinate = null, exception = null, isSuccess = false)
        }
   }
}