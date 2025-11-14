package ru.cherryngine.engine.core.commandmanager

import org.incendo.cloud.type.range.Range
import org.incendo.cloud.type.tuple.*

// Pair
operator fun <U : Any?, V : Any?> Pair<U, V>.component1(): U = first()
operator fun <U : Any?, V : Any?> Pair<U, V>.component2(): V = second()

// Triplet
operator fun <U : Any?, V : Any?, W : Any?> Triplet<U, V, W>.component1(): U = first()
operator fun <U : Any?, V : Any?, W : Any?> Triplet<U, V, W>.component2(): V = second()
operator fun <U : Any?, V : Any?, W : Any?> Triplet<U, V, W>.component3(): W = third()

// Quartet
operator fun <U : Any?, V : Any?, W : Any?, X : Any?> Quartet<U, V, W, X>.component1(): U = first()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?> Quartet<U, V, W, X>.component2(): V = second()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?> Quartet<U, V, W, X>.component3(): W = third()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?> Quartet<U, V, W, X>.component4(): X = fourth()

// Quintet
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?> Quintet<U, V, W, X, Y>.component1(): U = first()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?> Quintet<U, V, W, X, Y>.component2(): V = second()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?> Quintet<U, V, W, X, Y>.component3(): W = third()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?> Quintet<U, V, W, X, Y>.component4(): X = fourth()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?> Quintet<U, V, W, X, Y>.component5(): Y = fifth()

// Sextet
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component1(): U = first()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component2(): V = second()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component3(): W = third()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component4(): X = fourth()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component5(): Y = fifth()
operator fun <U : Any?, V : Any?, W : Any?, X : Any?, Y : Any?, Z : Any?> Sextet<U, V, W, X, Y, Z>.component6(): Z = sixth()

// Range
operator fun <N : Number> Range<N>.component1(): N = min()
operator fun <N : Number> Range<N>.component2(): N = max()