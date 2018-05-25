package com.programmerare.crsTransformations.compositeTransformations;

import com.programmerare.crsTransformations.CrsTransformationFacade;
import com.programmerare.crsTransformations.Coordinate;
import com.programmerare.crsTransformations.CrsIdentifier;
import com.programmerare.crsTransformations.crsConstants.ConstantEpsgNumber;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompositeStrategyForAverageValueTest extends CompositeStrategyTestBase {

    @Test
    void createCRStransformationFacadeAverage() {
        List<Coordinate> coordinateResultsForTheDifferentImplementations = Arrays.asList(resultCoordinateGeoTools, resultCoordinateGooberCTL, resultCoordinateOrbisgisCTS, resultCoordinateProj4J);
        Coordinate coordinateWithAverageLatitudeAndLongitude = calculateAverageCoordinate(coordinateResultsForTheDifferentImplementations);

        CrsTransformationFacade facadeComposite = CrsTransformationFacadeComposite.createCrsTransformationAverage(
            Arrays.asList(
                facadeGeoTools,
                facadeGooberCTL,
                facadeOrbisgisCTS,
                facadeProj4J
            )
        );
        Coordinate coordinateReturnedByCompositeFacade = facadeComposite.transform(wgs84coordinate, ConstantEpsgNumber.SWEREF99TM);

        double delta = 0.000000001;
        assertEquals(coordinateWithAverageLatitudeAndLongitude.getXLongitude(), coordinateReturnedByCompositeFacade.getXLongitude(), delta);
        assertEquals(coordinateWithAverageLatitudeAndLongitude.getYLatitude(), coordinateReturnedByCompositeFacade.getYLatitude(), delta);
        // assertEquals(coordinateWithAverageLatitudeAndLongitude, coordinateReturnedByCompositeFacade);
        // Expected :Coordinate(xLongitude=674032.3572074446, yLatitude=6580821.991903967, crsIdentifier=CrsIdentifier(crsCode=EPSG:3006, isEpsgCode=true, epsgNumber=3006))
        // Actual   :Coordinate(xLongitude=674032.3572074447, yLatitude=6580821.991903967, crsIdentifier=CrsIdentifier(crsCode=EPSG:3006, isEpsgCode=true, epsgNumber=3006))
    }

    private double getAverage(List<Coordinate> resultCoordinates, ToDoubleFunction<? super Coordinate> mapperReturningDoubleValueForAverageCalculation) {
        return resultCoordinates.stream().mapToDouble(mapperReturningDoubleValueForAverageCalculation).average().getAsDouble();
    }

    private Coordinate calculateAverageCoordinate(List<Coordinate> resultCoordinates) {
        double averageLat = getAverage(resultCoordinates, c -> c.getYLatitude());
        double averageLon = getAverage(resultCoordinates, c -> c.getXLongitude());
        Set<CrsIdentifier> set = resultCoordinates.stream().map(c -> c.getCrsIdentifier()).collect(Collectors.toSet());
        assertEquals(1, set.size(), "all coordinates should have the same CRS, since thet should all be the result of a transform to the same CRS");
        return Coordinate.createFromYLatXLong(averageLat, averageLon, resultCoordinates.get(0).getCrsIdentifier());
    }
}