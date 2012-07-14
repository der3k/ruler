package org.sikora.ruler.util

import spock.lang.Specification

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.7.12
 * Time: 0:54
 * To change this template use File | Settings | File Templates.
 */
class EditDistanceCalculatorTest extends Specification {
    def "should be zero for identical strings"() {
        expect:
        EditDistanceCalculator.of('task').distance('task') == 0
    }

    def "should be length for empty similar"() {
        expect:
        EditDistanceCalculator.of('task').distance("") == 4
    }

    def "should return Integer.MAX_VALUE when similar is longer than original"() {
        expect:
        EditDistanceCalculator.of('task').distance('taks1') == Integer.MAX_VALUE
    }

    def "should return edit distance"() {
        def calculator = EditDistanceCalculator.of('task')
        expect:
        calculator.distance('t') == 3
        calculator.distance('ta') == 2
        calculator.distance('tas') == 1
    }
}
