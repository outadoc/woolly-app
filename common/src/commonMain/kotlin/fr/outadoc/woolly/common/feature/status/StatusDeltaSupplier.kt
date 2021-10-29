package fr.outadoc.woolly.common.feature.status

interface StatusDeltaSupplier {
    fun onStatusAction(action: StatusAction)
}
