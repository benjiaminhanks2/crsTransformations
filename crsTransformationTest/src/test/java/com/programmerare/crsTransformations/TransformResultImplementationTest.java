package com.programmerare.crsTransformations;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.programmerare.crsTransformationFacadeGooberCTL.CrsTransformationFacadeGooberCTL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TransformResultImplementationTest {

    private TransformResult transformResult;
    private Coordinate inputCoordinate;
    private Coordinate outputCoordinate;

    private CrsTransformationFacade crsTransformationFacade;

    @BeforeEach
    void beforeEach() {
        inputCoordinate = Coordinate.createFromXLongitudeYLatitude(0.0, 0.0, 1234);
        outputCoordinate = inputCoordinate;

        // the mock below is currently not used any more than as a convenient way
        // of constructing an instance of the interface
        crsTransformationFacade = mock(CrsTransformationFacade.class);
    }

    @Test
    void transformNotSucessTest() {
        transformResult = new TransformResultImplementation(inputCoordinate, null, null, false, crsTransformationFacade, new ArrayList<TransformResult>());
        assertFalse(transformResult.isSuccess());
    }

    @Test
    void transformSucessTest() {
        transformResult = new TransformResultImplementation(inputCoordinate, outputCoordinate, null, true, crsTransformationFacade, new ArrayList<TransformResult>());
        assertTrue(transformResult.isSuccess());
    }

    @Test
    void unvalidTransformResultConstructionWhenSuccessIsFalseButCoordinateProvided() {
        assertThrows(
            IllegalStateException.class,
            () -> new TransformResultImplementation(inputCoordinate, outputCoordinate, null, false, crsTransformationFacade, new ArrayList<TransformResult>()),
            "unvalid TransformResult object construction should throw exception when success false is combined with output coordinate"
        );
    }

    @Test
    void unvalidTransformResultConstructionWhenSuccessIsTrueButCoordinateNotProvided() {
        outputCoordinate = null;
        assertThrows(
            IllegalStateException.class,
            () -> new TransformResultImplementation(inputCoordinate, outputCoordinate, null, true, crsTransformationFacade, new ArrayList<TransformResult>()),
            "unvalid TransformResult object construction should throw exception when success true is combined with null as output coordinate"
        );
    }

    @Test
    void preconditionViolationWhenTryingToAccessCoordinateWhenSuccessFalse() {
        outputCoordinate = null;
        transformResult = new TransformResultImplementation(inputCoordinate, outputCoordinate, null, false, crsTransformationFacade, new ArrayList<TransformResult>());
        Exception e = assertThrows(
            RuntimeException.class,
            () -> transformResult.getOutputCoordinate(),
            "Should not try to get output coordinate unless the result was a success"
        );
        String exceptionMessage = e.getMessage().toLowerCase().replaceAll("-", "");
        // the purpose of ther above is to make the test less sensitive to the exact message
        // e.g. will match text containing "Pre-condition" (including hyphen and uppercased first letter)
        assertThat(exceptionMessage, containsString("precondition"));
    }

    @Test
    void resultsStatisticTest() {
        double lat1 = 59.330231;
        double lat2 = 59.330232;
        double lat3 = 59.330233;
        double lat4 = 59.330239;
        double latMean = (lat2 + lat3 ) / 2;
        double latAverage = (lat1 + lat2 + lat3 + lat4) / 4;
        double expectedLatDiffMax = lat4-lat1;
        double lon1 = 18.059192;
        double lon2 = 18.059193;
        double lon3 = 18.059194;
        double lon4 = 18.059198;
        double lonMean = (lon2 + lon3 ) / 2;
        double lonAverage = (lon1 + lon2 + lon3 + lon4) / 4;
        double expectedLonDiffMax = lon4-lon1;
        Coordinate c1, c2, c3, c4, c;
        c1 = Coordinate.latLon(lat1, lon1);
        c2 = Coordinate.latLon(lat2, lon2);
        c3 = Coordinate.latLon(lat3, lon3);
        c4 = Coordinate.latLon(lat4, lon4);
        Coordinate expectedCoordinateMean = Coordinate.latLon(latMean, lonMean);
        Coordinate expectedCoordinateAverage = Coordinate.latLon(latAverage, lonAverage);


        c = Coordinate.latLon(0, 0); // input, not used here in this test
        CrsTransformationFacade f = new CrsTransformationFacadeGooberCTL(); // not used, might use a mock instead
        List<TransformResult>  l = Arrays.asList();


        final List<TransformResult> results = Arrays.asList(
            new TransformResultImplementation(c, c1, null, true, f, l),
            new TransformResultImplementation(c, c2, null, true, f, l),
            new TransformResultImplementation(c, c3, null, true, f, l),
            new TransformResultImplementation(c, c4, null, true, f, l)
        );
        final TransformResult transformResult = new TransformResultImplementation(c, c, null, true, f, results);

        final ResultsStatistic resultsStatistic = transformResult.getResultsStatistic();
        assertNotNull(resultsStatistic);
        assertTrue(resultsStatistic.isStatisticsAvailable());

        assertEquals(4, resultsStatistic.getNumberOfResults());
        assertEquals(expectedLatDiffMax, resultsStatistic.getMaxDiffLatitude());
        assertEquals(expectedLonDiffMax, resultsStatistic.getMaxDiffLongitude());

        final Coordinate coordinateAverage = resultsStatistic.getCoordinateAverage();
        final Coordinate coordinateMean = resultsStatistic.getCoordinateMean();
        assertNotNull(coordinateAverage);
        assertNotNull(coordinateMean);
        assertEquals(expectedCoordinateMean, coordinateMean);
        assertEquals(expectedCoordinateAverage, coordinateAverage);
    }

}