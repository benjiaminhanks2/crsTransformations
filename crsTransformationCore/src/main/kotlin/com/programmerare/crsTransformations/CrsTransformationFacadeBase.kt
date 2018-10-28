package com.programmerare.crsTransformations

abstract class CrsTransformationFacadeBase : CrsTransformationFacade {

    override final fun transformToCoordinate(
        inputCoordinate: Coordinate,
        crsCodeForOutputCoordinateSystem: String
    ): Coordinate {
        // this Template Method is invoking the below overloaded hook method in subclasses
        return transformHook(
            inputCoordinate,
            CrsIdentifier.createFromCrsCode(crsCodeForOutputCoordinateSystem)
        )
    }

    override final fun transformToCoordinate(
        inputCoordinate: Coordinate,
        epsgNumberForOutputCoordinateSystem: Int
    ): Coordinate {
        return transformHook(
            inputCoordinate,
            CrsIdentifier.createFromEpsgNumber(epsgNumberForOutputCoordinateSystem)
        )
    }

    override final fun transformToCoordinate(
        inputCoordinate: Coordinate,
        crsIdentifierForOutputCoordinateSystem: CrsIdentifier
    ): Coordinate {
        return transformHook(
            inputCoordinate,
            crsIdentifierForOutputCoordinateSystem
        )
    }

    abstract protected fun transformHook(
        inputCoordinate: Coordinate,
        crsIdentifierForOutputCoordinateSystem: CrsIdentifier
    ): Coordinate


    override final fun transform(
        inputCoordinate: Coordinate,
        epsgNumberForOutputCoordinateSystem: Int
    ): TransformResult {
        return transform(
            inputCoordinate,
            CrsIdentifier.createFromEpsgNumber(epsgNumberForOutputCoordinateSystem)
        )
    }

    override final fun transform(
        inputCoordinate: Coordinate,
        crsCodeForOutputCoordinateSystem: String
    ): TransformResult {
        return transform(
            inputCoordinate,
            CrsIdentifier.createFromCrsCode(crsCodeForOutputCoordinateSystem)
        )
    }

//    override final fun transform(
//        inputCoordinate: Coordinate,
//        crsIdentifierForOutputCoordinateSystem: CrsIdentifier
//    ): TransformResult {
//        return transformHook(inputCoordinate, CrsIdentifier.createFromCrsCode(crsCodeForOutputCoordinateSystem))
//    }


    override final fun getNameOfImplementation(): String {
        return this.javaClass.name
    }

}