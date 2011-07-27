package org.rsi.kcommander

/**
 * User: sikorric
 * Date: 11.7.11
 * Time: 14:20
 */


def input = 'timer  in 20 minutes finish'
println input
def draft = new TaskDraft(input, 15)
println draft
println draft.current
println draft.text()
def completed = draft.complete('miracle')
println completed
println completed.text()
println 'ok.'