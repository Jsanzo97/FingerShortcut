package com.jsanzo.figerprintshortcut

import android.service.quicksettings.Tile.STATE_ACTIVE
import android.service.quicksettings.Tile.STATE_INACTIVE
import android.service.quicksettings.TileService
import kotlin.concurrent.thread

class QuickSettingsService : TileService() {

    override fun onClick() {
        super.onClick()
        when (qsTile.state) {
            STATE_ACTIVE -> qsTile.state = STATE_INACTIVE
            STATE_INACTIVE -> {
                qsTile.state = STATE_ACTIVE
                thread {
                    while(true) {
                        println("test")
                        Thread.sleep(1000)
                    }

                }
            }
            else -> {}
        }
        qsTile.updateTile()
    }

}