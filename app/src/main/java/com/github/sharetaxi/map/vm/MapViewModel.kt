package com.github.sharetaxi.map.vm

import com.github.sharetaxi.general.repo.SearchRepository
import com.github.sharetaxi.map.MapViewState
import com.google.android.gms.maps.model.LatLng
import com.mvi.vm.MviBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch

class MapViewModel(
    private val searchRepository: SearchRepository
) :
    MviBaseViewModel<MapViewState, MapViewModel.StateChanges>(
        bgDispatcher = Dispatchers.IO,
        uiDispatcher = Dispatchers.Main
    ) {
    override val initialViewState = MapViewState()
    private val placeFromSelectedIntent = ConflatedBroadcastChannel<LatLng>()
    private val placeToSelectedIntent = ConflatedBroadcastChannel<LatLng>()
    private val searchIntent = ConflatedBroadcastChannel<Unit>()

    override suspend fun bindIntentsActual(): Array<ReceiveChannel<StateChanges>> {
        val chan = produce {
            var fromLatLng: LatLng? = null
            var toLatLng: LatLng? = null
            launch {
                placeFromSelectedIntent.openSubscription().consumeEach {
                    fromLatLng = it
                }
            }

            launch {
                placeToSelectedIntent.openSubscription().consumeEach {
                    toLatLng = it
                }
            }

            launch {
                searchIntent.openSubscription().consumeEach {
                    try {
                        val result = searchRepository.search(fromLatLng, toLatLng)
                        send(StateChanges.SuccessSearch())
                    } catch (e: Exception) {
                        send(StateChanges.ErrorSearch(e))
                    }
                }
            }
        }
        return arrayOf(chan)
    }

    override fun handleStateChanges(
        previousState: MapViewState,
        stateChanges: StateChanges
    ): MapViewState {
        return previousState
    }

    fun placeFromSelected(selection: LatLng) = placeFromSelectedIntent.offer(selection)

    fun placeToSelected(selection: LatLng) = placeToSelectedIntent.offer(selection)

    fun performSearch() = searchIntent.offer(Unit)

    sealed class StateChanges {
        class SuccessSearch() : StateChanges()
        class ErrorSearch(private val e: Exception) : StateChanges()
    }
}
