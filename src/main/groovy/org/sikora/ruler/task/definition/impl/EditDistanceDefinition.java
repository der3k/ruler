package org.sikora.ruler.task.definition.impl;

import org.sikora.ruler.context.InputEventInContext;
import org.sikora.ruler.util.EditDistanceCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.7.12
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class EditDistanceDefinition extends SimpleDefinition {
    @Override
    public Match match(final InputEventInContext inputEventInContext) {
        final String name = name().toLowerCase();
        final String similar = inputEventInContext.input().activeText().toLowerCase();
        if (similar.isEmpty())
            return Match.of(Match.NONE, this);
        if (name.equals(similar))
            return Match.of(Match.EXACT, this);
        final int distance = EditDistanceCalculator.of(name).distance(similar);
        if (distance > name.length())
            return Match.of(Match.NONE, this);
        final BigDecimal fraction = BigDecimal.valueOf(distance).divide(BigDecimal.valueOf(name.length()), 2, RoundingMode.HALF_UP);
        final BigDecimal invertedFraction = BigDecimal.ONE.subtract(fraction);
        final int match = invertedFraction.multiply(BigDecimal.valueOf(100)).intValue();
        return Match.of(match % 100, this);
    }
}
