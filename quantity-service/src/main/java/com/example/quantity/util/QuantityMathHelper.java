package com.example.quantity.util;

import com.example.quantity.exception.QuantityMeasurementException;
import java.util.Locale;

public final class QuantityMathHelper {
    private static final double EPSILON = 1e-6;

    private QuantityMathHelper() {
    }

    public static boolean compare(double thisValue, String thisUnit, String thisMeasurementType,
            double thatValue, String thatUnit, String thatMeasurementType) {
        ensureSameMeasurementType(thisMeasurementType, thatMeasurementType, "compare");
        double convertedThatValue = convert(thatValue, thatUnit, thisUnit, thisMeasurementType);
        return Math.abs(thisValue - convertedThatValue) <= EPSILON;
    }

    public static double convert(double value, String fromUnit, String toUnit, String measurementType) {
        String normalizedMeasurementType = normalizeMeasurementType(measurementType);
        String normalizedFromUnit = normalizeUnit(fromUnit);
        String normalizedToUnit = normalizeUnit(toUnit);

        return switch (normalizedMeasurementType) {
            case "LengthUnit" -> {
                double baseMeters = toMeters(value, normalizedFromUnit);
                yield fromMeters(baseMeters, normalizedToUnit);
            }
            case "WeightUnit" -> {
                double baseKilograms = toKilograms(value, normalizedFromUnit);
                yield fromKilograms(baseKilograms, normalizedToUnit);
            }
            case "VolumeUnit" -> {
                double baseLiters = toLiters(value, normalizedFromUnit);
                yield fromLiters(baseLiters, normalizedToUnit);
            }
            case "TemperatureUnit" -> {
                double baseCelsius = toCelsius(value, normalizedFromUnit);
                yield fromCelsius(baseCelsius, normalizedToUnit);
            }
            case "AreaUnit" -> {
                double baseSqm = toSquareMeters(value, normalizedFromUnit);
                yield fromSquareMeters(baseSqm, normalizedToUnit);
            }
            case "TimeUnit" -> {
                double baseSeconds = toSeconds(value, normalizedFromUnit);
                yield fromSeconds(baseSeconds, normalizedToUnit);
            }
            case "SpeedUnit" -> {
                double baseMps = toMetersPerSecond(value, normalizedFromUnit);
                yield fromMetersPerSecond(baseMps, normalizedToUnit);
            }
            default -> throw new QuantityMeasurementException("Unsupported measurement type: " + measurementType);
        };
    }

    public static double add(double thisValue, String thisUnit, String thisMeasurementType,
            double thatValue, String thatUnit, String thatMeasurementType) {
        ensureSameMeasurementType(thisMeasurementType, thatMeasurementType, "add");
        double convertedThatValue = convert(thatValue, thatUnit, thisUnit, thisMeasurementType);
        return thisValue + convertedThatValue;
    }

    public static double subtract(double thisValue, String thisUnit, String thisMeasurementType,
            double thatValue, String thatUnit, String thatMeasurementType) {
        ensureSameMeasurementType(thisMeasurementType, thatMeasurementType, "subtract");
        double convertedThatValue = convert(thatValue, thatUnit, thisUnit, thisMeasurementType);
        return thisValue - convertedThatValue;
    }

    public static double multiply(double thisValue, String thisUnit, String thisMeasurementType,
            double thatValue, String thatUnit, String thatMeasurementType) {
        ensureSameMeasurementType(thisMeasurementType, thatMeasurementType, "multiply");
        double convertedThatValue = convert(thatValue, thatUnit, thisUnit, thisMeasurementType);
        return thisValue * convertedThatValue;
    }

    public static double divide(double thisValue, String thisUnit, String thisMeasurementType,
            double thatValue, String thatUnit, String thatMeasurementType) {
        ensureSameMeasurementType(thisMeasurementType, thatMeasurementType, "divide");
        double convertedThatValue = convert(thatValue, thatUnit, thisUnit, thisMeasurementType);
        if (Math.abs(convertedThatValue) <= EPSILON) {
            throw new ArithmeticException("Divide by zero");
        }
        return thisValue / convertedThatValue;
    }

    public static boolean isValidUnitForMeasurementType(String unit, String measurementType) {
        if (unit == null || measurementType == null) {
            return false;
        }

        try {
            String normalizedMeasurementType = normalizeMeasurementType(measurementType);
            String normalizedUnit = normalizeUnit(unit);
            return switch (normalizedMeasurementType) {
                case "LengthUnit" -> isLengthUnit(normalizedUnit);
                case "WeightUnit" -> isWeightUnit(normalizedUnit);
                case "VolumeUnit" -> isVolumeUnit(normalizedUnit);
                case "TemperatureUnit" -> isTemperatureUnit(normalizedUnit);
                case "AreaUnit" -> isAreaUnit(normalizedUnit);
                case "TimeUnit" -> isTimeUnit(normalizedUnit);
                case "SpeedUnit" -> isSpeedUnit(normalizedUnit);
                default -> false;
            };
        } catch (QuantityMeasurementException exception) {
            return false;
        }
    }

    public static String normalizeMeasurementType(String measurementType) {
        if (measurementType == null || measurementType.trim().isEmpty()) {
            throw new QuantityMeasurementException("Measurement type is required.");
        }

        String normalized = measurementType.trim().toUpperCase(Locale.ROOT);
        if (normalized.equals("LENGTH") || normalized.equals("LENGTHUNIT")) {
            return "LengthUnit";
        }
        if (normalized.equals("WEIGHT") || normalized.equals("WEIGHTUNIT")) {
            return "WeightUnit";
        }
        if (normalized.equals("VOLUME") || normalized.equals("VOLUMEUNIT")) {
            return "VolumeUnit";
        }
        if (normalized.equals("TEMPERATURE") || normalized.equals("TEMPERATUREUNIT")) {
            return "TemperatureUnit";
        }
        if (normalized.equals("AREA") || normalized.equals("AREAUNIT")) {
            return "AreaUnit";
        }
        if (normalized.equals("TIME") || normalized.equals("TIMEUNIT")) {
            return "TimeUnit";
        }
        if (normalized.equals("SPEED") || normalized.equals("SPEEDUNIT")) {
            return "SpeedUnit";
        }

        throw new QuantityMeasurementException("Invalid measurement type: " + measurementType);
    }

    private static void ensureSameMeasurementType(String thisMeasurementType, String thatMeasurementType,
            String operationName) {
        String thisType = normalizeMeasurementType(thisMeasurementType);
        String thatType = normalizeMeasurementType(thatMeasurementType);
        if (!thisType.equals(thatType)) {
            throw new QuantityMeasurementException(operationName + " Error: Cannot perform arithmetic between different "
                    + "measurement categories: " + thisType + " and " + thatType);
        }
    }

    private static String normalizeUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new QuantityMeasurementException("Unit name is required.");
        }
        return unit.trim().toUpperCase(Locale.ROOT);
    }

    private static double toMeters(double value, String unit) {
        return switch (unit) {
            case "MILLIMETER", "MILLIMETERS", "MM" -> value * 0.001;
            case "CENTIMETER", "CENTIMETERS", "CM" -> value * 0.01;
            case "METER", "METERS", "M" -> value;
            case "KILOMETER", "KILOMETERS", "KM" -> value * 1000.0;
            case "INCH", "INCHES" -> value * 0.0254;
            case "FOOT", "FEET" -> value * 0.3048;
            case "YARD", "YARDS" -> value * 0.9144;
            case "MILE", "MILES" -> value * 1609.344;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromMeters(double value, String unit) {
        return switch (unit) {
            case "MILLIMETER", "MILLIMETERS", "MM" -> value / 0.001;
            case "CENTIMETER", "CENTIMETERS", "CM" -> value / 0.01;
            case "METER", "METERS", "M" -> value;
            case "KILOMETER", "KILOMETERS", "KM" -> value / 1000.0;
            case "INCH", "INCHES" -> value / 0.0254;
            case "FOOT", "FEET" -> value / 0.3048;
            case "YARD", "YARDS" -> value / 0.9144;
            case "MILE", "MILES" -> value / 1609.344;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toKilograms(double value, String unit) {
        return switch (unit) {
            case "MILLIGRAM", "MILLIGRAMS", "MG" -> value * 1e-6;
            case "GRAM", "GRAMS", "G" -> value * 0.001;
            case "KILOGRAM", "KILOGRAMS", "KG" -> value;
            case "TON", "TONS" -> value * 1000.0;
            case "OUNCE", "OUNCES", "OZ" -> value * 0.0283495;
            case "POUND", "POUNDS", "LB", "LBS" -> value * 0.45359237;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromKilograms(double value, String unit) {
        return switch (unit) {
            case "MILLIGRAM", "MILLIGRAMS", "MG" -> value / 1e-6;
            case "GRAM", "GRAMS", "G" -> value / 0.001;
            case "KILOGRAM", "KILOGRAMS", "KG" -> value;
            case "TON", "TONS" -> value / 1000.0;
            case "OUNCE", "OUNCES", "OZ" -> value / 0.0283495;
            case "POUND", "POUNDS", "LB", "LBS" -> value / 0.45359237;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toLiters(double value, String unit) {
        return switch (unit) {
            case "MILLILITER", "MILLILITERS", "ML" -> value * 0.001;
            case "LITER", "LITERS", "L" -> value;
            case "CUBIC_METER", "CUBIC_METERS", "M3" -> value * 1000.0;
            case "FLUID_OUNCE", "FLUID_OUNCES", "FL_OZ" -> value * 0.0295735;
            case "CUP", "CUPS" -> value * 0.24;
            case "PINT", "PINTS" -> value * 0.473176;
            case "QUART", "QUARTS" -> value * 0.946353;
            case "GALLON", "GALLONS" -> value * 3.78541;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromLiters(double value, String unit) {
        return switch (unit) {
            case "MILLILITER", "MILLILITERS", "ML" -> value / 0.001;
            case "LITER", "LITERS", "L" -> value;
            case "CUBIC_METER", "CUBIC_METERS", "M3" -> value / 1000.0;
            case "FLUID_OUNCE", "FLUID_OUNCES", "FL_OZ" -> value / 0.0295735;
            case "CUP", "CUPS" -> value / 0.24;
            case "PINT", "PINTS" -> value / 0.473176;
            case "QUART", "QUARTS" -> value / 0.946353;
            case "GALLON", "GALLONS" -> value / 3.78541;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toCelsius(double value, String unit) {
        return switch (unit) {
            case "CELSIUS", "C" -> value;
            case "FAHRENHEIT", "F" -> (value - 32.0) * 5.0 / 9.0;
            case "KELVIN", "K" -> value - 273.15;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromCelsius(double value, String unit) {
        return switch (unit) {
            case "CELSIUS", "C" -> value;
            case "FAHRENHEIT", "F" -> (value * 9.0 / 5.0) + 32.0;
            case "KELVIN", "K" -> value + 273.15;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toSquareMeters(double value, String unit) {
        return switch (unit) {
            case "SQUARE_METER", "SQUARE_METERS" -> value;
            case "SQUARE_KILOMETER", "SQUARE_KILOMETERS" -> value * 1_000_000.0;
            case "SQUARE_FOOT", "SQUARE_FEET" -> value * 0.092903;
            case "SQUARE_INCH", "SQUARE_INCHES" -> value * 0.00064516;
            case "ACRE", "ACRES" -> value * 4046.86;
            case "HECTARE", "HECTARES" -> value * 10000.0;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromSquareMeters(double value, String unit) {
        return switch (unit) {
            case "SQUARE_METER", "SQUARE_METERS" -> value;
            case "SQUARE_KILOMETER", "SQUARE_KILOMETERS" -> value / 1_000_000.0;
            case "SQUARE_FOOT", "SQUARE_FEET" -> value / 0.092903;
            case "SQUARE_INCH", "SQUARE_INCHES" -> value / 0.00064516;
            case "ACRE", "ACRES" -> value / 4046.86;
            case "HECTARE", "HECTARES" -> value / 10000.0;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toSeconds(double value, String unit) {
        return switch (unit) {
            case "SECOND", "SECONDS" -> value;
            case "MINUTE", "MINUTES" -> value * 60.0;
            case "HOUR", "HOURS" -> value * 3600.0;
            case "DAY", "DAYS" -> value * 86400.0;
            case "WEEK", "WEEKS" -> value * 604800.0;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromSeconds(double value, String unit) {
        return switch (unit) {
            case "SECOND", "SECONDS" -> value;
            case "MINUTE", "MINUTES" -> value / 60.0;
            case "HOUR", "HOURS" -> value / 3600.0;
            case "DAY", "DAYS" -> value / 86400.0;
            case "WEEK", "WEEKS" -> value / 604800.0;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double toMetersPerSecond(double value, String unit) {
        return switch (unit) {
            case "METER_PER_SECOND", "MPS" -> value;
            case "KILOMETER_PER_HOUR", "KPH" -> value / 3.6;
            case "MILE_PER_HOUR", "MPH" -> value * 0.44704;
            case "KNOT", "KNOTS" -> value * 0.514444;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static double fromMetersPerSecond(double value, String unit) {
        return switch (unit) {
            case "METER_PER_SECOND", "MPS" -> value;
            case "KILOMETER_PER_HOUR", "KPH" -> value * 3.6;
            case "MILE_PER_HOUR", "MPH" -> value / 0.44704;
            case "KNOT", "KNOTS" -> value / 0.514444;
            default -> throw new QuantityMeasurementException("Invalid unit name: " + unit + ".");
        };
    }

    private static boolean isLengthUnit(String unit) {
        return switch (unit) {
            case "MILLIMETER", "MILLIMETERS", "MM", "CENTIMETER", "CENTIMETERS", "CM",
                    "METER", "METERS", "M", "KILOMETER", "KILOMETERS", "KM",
                    "INCH", "INCHES", "FOOT", "FEET", "YARD", "YARDS", "MILE", "MILES" -> true;
            default -> false;
        };
    }

    private static boolean isWeightUnit(String unit) {
        return switch (unit) {
            case "MILLIGRAM", "MILLIGRAMS", "MG", "GRAM", "GRAMS", "G",
                    "KILOGRAM", "KILOGRAMS", "KG", "TON", "TONS",
                    "OUNCE", "OUNCES", "OZ", "POUND", "POUNDS", "LB", "LBS" -> true;
            default -> false;
        };
    }

    private static boolean isVolumeUnit(String unit) {
        return switch (unit) {
            case "MILLILITER", "MILLILITERS", "ML", "LITER", "LITERS", "L",
                    "CUBIC_METER", "CUBIC_METERS", "M3", "FLUID_OUNCE", "FLUID_OUNCES", "FL_OZ",
                    "CUP", "CUPS", "PINT", "PINTS", "QUART", "QUARTS", "GALLON", "GALLONS" -> true;
            default -> false;
        };
    }

    private static boolean isTemperatureUnit(String unit) {
        return switch (unit) {
            case "CELSIUS", "C", "FAHRENHEIT", "F", "KELVIN", "K" -> true;
            default -> false;
        };
    }

    private static boolean isAreaUnit(String unit) {
        return switch (unit) {
            case "SQUARE_METER", "SQUARE_METERS", "SQUARE_KILOMETER", "SQUARE_KILOMETERS",
                    "SQUARE_FOOT", "SQUARE_FEET", "SQUARE_INCH", "SQUARE_INCHES",
                    "ACRE", "ACRES", "HECTARE", "HECTARES" -> true;
            default -> false;
        };
    }

    private static boolean isTimeUnit(String unit) {
        return switch (unit) {
            case "SECOND", "SECONDS", "MINUTE", "MINUTES", "HOUR", "HOURS",
                    "DAY", "DAYS", "WEEK", "WEEKS" -> true;
            default -> false;
        };
    }

    private static boolean isSpeedUnit(String unit) {
        return switch (unit) {
            case "METER_PER_SECOND", "MPS", "KILOMETER_PER_HOUR", "KPH",
                    "MILE_PER_HOUR", "MPH", "KNOT", "KNOTS" -> true;
            default -> false;
        };
    }
}