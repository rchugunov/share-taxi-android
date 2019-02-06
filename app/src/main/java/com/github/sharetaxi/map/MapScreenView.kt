package com.github.sharetaxi.map

import com.mvi.view.MVIView

interface MapScreenView : MVIView<MapViewState>

class MapViewState() : MVIView.ViewState