package com.programmerare.crsTransformations;

import com.programmerare.crsTransformationFacadeGeoTools.CRStransformationFacadeGeoTools;
import com.programmerare.crsTransformationFacadeGooberCTL.CRStransformationFacadeGooberCTL;
import com.programmerare.crsTransformationFacadeOrbisgisCTS.CRStransformationFacadeOrbisgisCTS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class CRStransformationFacadeTest {

    private final static int epsgNumberForWgs84 = 4326;
    private final static int epsgNumberForRT90 = 3021;
    private final static int epsgNumberForSweref99TM = 3006;
    private final static int epsgNumberForSweref991200 = 3007; // EPSG:3007: SWEREF99 12 00	https://epsg.io/3007
    private final static int epsgNumberForSweref991500 = 3009; // EPSG:3009: SWEREF99 15 00	https://epsg.io/3009

    // TODO: Define this constant somewhere else ...  maybe in CRStransformationFacadeGooberCTL which aer also using these EPSG values ...
    private final static int lowerEpsgIntervalForSwedishProjectionsUsingMeterAsUnit = 3006;
    private final static int upperEpsgIntervalForSwedishProjectionsUsingMeterAsUnit = 3024;

    private static List<Integer> epsgNumbersForSwedishProjectionsUsingMeterAsUnit;

    private final static List<CRStransformationFacade> crsTransformationFacadeImplementations = Arrays.asList(
        new CRStransformationFacadeGeoTools(),
        new CRStransformationFacadeGooberCTL(),
        new CRStransformationFacadeOrbisgisCTS()
    );

    @BeforeAll
    static void beforeAll() {
        epsgNumbersForSwedishProjectionsUsingMeterAsUnit = IntStream.rangeClosed(lowerEpsgIntervalForSwedishProjectionsUsingMeterAsUnit, upperEpsgIntervalForSwedishProjectionsUsingMeterAsUnit).boxed().collect(toList());
    }

    @Test
    void transformationFromWgs84ToSweref99TM() {
        for (CRStransformationFacade crsTransformationFacade : crsTransformationFacadeImplementations) {
            testTransformationFromWgs84ToSweref99TM(crsTransformationFacade);
        }
    }

    @Test
    void transformationFromRT90ToWgs84() {
        for (CRStransformationFacade crsTransformationFacade : crsTransformationFacadeImplementations) {
            testTransformationFromRT90ToWgs84(crsTransformationFacade);
        }
    }



    @Test
    void transformationFromSweref991200ToSweref991500() {
        for (CRStransformationFacade crsTransformationFacade : crsTransformationFacadeImplementations) {
            testTransformationFromSweref991200ToSweref991500(crsTransformationFacade);
        }
    }

    private void testTransformationFromSweref991200ToSweref991500(CRStransformationFacade crsTransformationFacade) {
        double sweref1200_Y = 6580822;
        double sweref1200_X = 674032;

        // transform back and forth (from sweref1200 to sweref1500 and then back to sweref1200)
        // and then check if you got the same as the original sweref1200
        Coordinate inputCoordinateSweref1200 = Coordinate.createFromXLongYLat(sweref1200_X, sweref1200_Y, epsgNumberForSweref991200);
        transformBackAndForthAndAssertResult(crsTransformationFacade, inputCoordinateSweref1200, epsgNumberForSweref991500);
    }

    private void transformBackAndForthAndAssertResult(
        CRStransformationFacade crsTransformationFacade,
        Coordinate inputCoordinateOriginalCRS,
        int epsgNumberForTransformTargetCRS
    ) {
        double delta = getDeltaValueForComparisons(inputCoordinateOriginalCRS.getEpsgNumber());

        Coordinate outputCoordinateForTransformTargetCRS = crsTransformationFacade.transform(inputCoordinateOriginalCRS, epsgNumberForTransformTargetCRS);
        Coordinate outputCoordinateOriginalCRS = crsTransformationFacade.transform(outputCoordinateForTransformTargetCRS, inputCoordinateOriginalCRS.getEpsgNumber());

        assertEquals(inputCoordinateOriginalCRS.getXLongitude(), outputCoordinateOriginalCRS.getXLongitude(), delta);
        assertEquals(inputCoordinateOriginalCRS.getYLatitude(), outputCoordinateOriginalCRS.getYLatitude(), delta);
        assertEquals(inputCoordinateOriginalCRS.getEpsgNumber(), outputCoordinateOriginalCRS.getEpsgNumber());
    }

    private void testTransformationFromWgs84ToSweref99TM(CRStransformationFacade crsTransformationFacade) {
        // This test is using the coordinates of Stockholm Centralstation (Sweden)
        // https://kartor.eniro.se/m/03Yxp
        // WGS84 decimal (lat, lon)
        // 59.330231, 18.059196
        // SWEREF99 TM (nord, öst)
        // 6580822, 674032

        double wgs84Lat = 59.330231;
        double wgs84Lon = 18.059196;

        double sweref99_Y_expected = 6580822;
        double sweref99_X_expected = 674032;

        Coordinate inputCoordinate = Coordinate.createFromXLongYLat(wgs84Lon, wgs84Lat, epsgNumberForWgs84);
        Coordinate outputCoordinate = crsTransformationFacade.transform(inputCoordinate, epsgNumberForSweref99TM);
        assertEquals(sweref99_Y_expected, outputCoordinate.getYLatitude(), 0.5);
        assertEquals(sweref99_X_expected, outputCoordinate.getXLongitude(), 0.5);
    }

    private void testTransformationFromRT90ToWgs84(CRStransformationFacade crsTransformationFacade) {
        double wgs84Lat_expected = 59.330231;
        double wgs84Lon_expected = 18.059196;

        double rt90_Y = 6580994;
        double rt90_X = 1628294;

        Coordinate inputCoordinate = Coordinate.createFromXLongYLat(rt90_X, rt90_Y, epsgNumberForRT90);

        Coordinate outputCoordinate = crsTransformationFacade.transform(inputCoordinate, epsgNumberForWgs84);
        assertEquals(wgs84Lat_expected, outputCoordinate.getYLatitude(), 0.1);
        assertEquals(wgs84Lon_expected, outputCoordinate.getXLongitude(), 0.1);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/coordinatesForSweden.csv", numLinesToSkip = 3, delimiter = ';')
    @DisplayName("Transformation result coordinates should match with coordinates in CSV file")
    void verifyTransformationsCorrespondToCsvFileCoordinates(
        String description,
        double wgs84Lat, double wgs84Lon,
        double rt90nord, double rt90ost,
        double sweref99nord, double sweref99ost,
        String url
    ) {
        // example row from the csv file:
        // Stockholm Centralstation;59.330231;18.059196;6580994;1628294;6580822;674032;https://kartor.eniro.se/m/03Yxp

        // These used coordinates (i.e. those in the csv file) were manually retrieved from the Eniro
        // site at the URL's for each row, and by clicking the coordinate feature
        // which shows the coordinates in the three systems WGS84, RT90, SWREF99

        for (CRStransformationFacade crsTransformationFacade : crsTransformationFacadeImplementations) {
            testTransformation(crsTransformationFacade, epsgNumberForWgs84, epsgNumberForRT90, wgs84Lat, wgs84Lon, rt90nord, rt90ost, description);
            testTransformation(crsTransformationFacade, epsgNumberForWgs84, epsgNumberForSweref99TM, wgs84Lat, wgs84Lon, sweref99nord, sweref99ost, description);
            testTransformation(crsTransformationFacade, epsgNumberForRT90, epsgNumberForSweref99TM, rt90nord, rt90ost, sweref99nord, sweref99ost, description);
        }
    }

    private void testTransformation(CRStransformationFacade crsTransformationFacade, int epsgNumber1, int epsgNumber2, double lat1, double lon1, double lat2, double lon2, String description) {
        final Coordinate coordinate1 = Coordinate.createFromXLongYLat(lon1, lat1, epsgNumber1);
        final Coordinate coordinate2 = Coordinate.createFromXLongYLat(lon2, lat2, epsgNumber2);
        final Coordinate outputForCoordinate1 = crsTransformationFacade.transform(coordinate1, epsgNumber2);
        final Coordinate outputForCoordinate2 = crsTransformationFacade.transform(coordinate2, epsgNumber1);

        double delta = getDeltaValueForComparisons(epsgNumber2);
        assertEquals(coordinate2.getXLongitude(), outputForCoordinate1.getXLongitude(), delta, description);
        assertEquals(coordinate2.getYLatitude(), outputForCoordinate1.getYLatitude(), delta, description);

        delta = getDeltaValueForComparisons(epsgNumber1);
        assertEquals(coordinate1.getXLongitude(), outputForCoordinate2.getXLongitude(), delta, description);
        assertEquals(coordinate1.getYLatitude(), outputForCoordinate2.getYLatitude(), delta, description);
    }

    private double getDeltaValueForComparisons(int epsgNumber) {
        CoordinateReferenceSystemUnit coordinateReferenceSystemUnit = CoordinateReferenceSystemUnit.UNKNOWN;
        if(epsgNumber == epsgNumberForWgs84) {
            coordinateReferenceSystemUnit = CoordinateReferenceSystemUnit.DEGREES;
        }
        // sweref : 3006 - 3018
        // RT90 :   3019 - 3024
        else if(
            lowerEpsgIntervalForSwedishProjectionsUsingMeterAsUnit <= epsgNumber
            &&
            epsgNumber <= upperEpsgIntervalForSwedishProjectionsUsingMeterAsUnit
        ) {
            coordinateReferenceSystemUnit = CoordinateReferenceSystemUnit.METERS;
        }
        return getDeltaValueForComparisons(coordinateReferenceSystemUnit, epsgNumber);
    }

    private double getDeltaValueForComparisons(CoordinateReferenceSystemUnit coordinateReferenceSystemUnit, int epsgNumberUsedOnlyInErrorMessage) {
        if(coordinateReferenceSystemUnit == CoordinateReferenceSystemUnit.DEGREES) {
            // one of the results:
            // Expected :20.266843
            // Actual   :20.266853924440145
            // the above diff is about 0.0001
            return 0.0001;
        }
        else if(coordinateReferenceSystemUnit == CoordinateReferenceSystemUnit.METERS) {
            // one of the results:
            // Expected :386087.0
            // Actual   :386088.0820856609
            // the above diff is about 1.08
            return 1.1;
        }
        else { // if(coordinateReferenceSystemUnit == CoordinateReferenceSystemUnit.UNKNOWN) {
            throw new IllegalArgumentException("Not supported epsg number: " + epsgNumberUsedOnlyInErrorMessage);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/coordinatesForSweden.csv", numLinesToSkip = 3, delimiter = ';')
    @DisplayName("Transformation back and forth from WGS84 cordinates to RT90/SWEREF99 projections should result in the same WGS84 coordinates")
    void verifyTransformationsBackAndForthFromWgs84ToSwedishProjections(
        String description,
        double wgs84Lat, double wgs84Lon // ignore the rest of columns for this test method
    ) {
       Coordinate inputCoordinateWGS84 = Coordinate.createFromXLongYLat(wgs84Lon, wgs84Lat, epsgNumberForWgs84);
        for (CRStransformationFacade crsTransformationFacade : crsTransformationFacadeImplementations) {
            for (Integer epsgNumber : epsgNumbersForSwedishProjectionsUsingMeterAsUnit) {
                transformBackAndForthAndAssertResult(crsTransformationFacade, inputCoordinateWGS84, epsgNumber);
            }
        }
    }

    private void transformWithTwoImplementationsAndCompareTheResults(
        CRStransformationFacade crsTransformationFacade1,
        CRStransformationFacade crsTransformationFacade2,
        Coordinate inputCoordinate,
        int epsgNumberForOutputCoordinate
    ) {
        double delta = getDeltaValueForComparisons(epsgNumberForOutputCoordinate);

        Coordinate outputCoordinate1 = crsTransformationFacade1.transform(inputCoordinate, epsgNumberForOutputCoordinate);
        Coordinate outputCoordinate2 = crsTransformationFacade2.transform(inputCoordinate, epsgNumberForOutputCoordinate);

        Supplier<String> errorMessage = () -> "delta used: " + delta + " and the diff was " + Math.abs(outputCoordinate1.getXLongitude() - outputCoordinate2.getXLongitude());
        assertEquals(outputCoordinate1.getXLongitude(), outputCoordinate2.getXLongitude(), delta, errorMessage);
        assertEquals(outputCoordinate1.getYLatitude(), outputCoordinate2.getYLatitude(), delta, errorMessage);
        assertEquals(outputCoordinate1.getEpsgNumber(), outputCoordinate2.getEpsgNumber());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/coordinatesForSweden.csv", numLinesToSkip = 3, delimiter = ';')
    @DisplayName("The same transformation but with different implementations should produce the same coordinates")
    void verifyTransformationResultsAreTheSameWithDifferentImplementations(
        String description,
        double wgs84Lat, double wgs84Lon // ignore the rest of columns for this test method
    ) {
        Coordinate inputCoordinateWGS84 = Coordinate.createFromXLongYLat(wgs84Lon, wgs84Lat, epsgNumberForWgs84);
        for (int i = 0; i < crsTransformationFacadeImplementations.size()-1; i++) {
            for (int j = i+1; j < crsTransformationFacadeImplementations.size(); j++) {
                for (Integer epsgNumber : epsgNumbersForSwedishProjectionsUsingMeterAsUnit) {
                    transformWithTwoImplementationsAndCompareTheResults(
                        crsTransformationFacadeImplementations.get(i),
                        crsTransformationFacadeImplementations.get(j),
                        inputCoordinateWGS84,
                        epsgNumber
                    );
                }
            }
        }
    }
}