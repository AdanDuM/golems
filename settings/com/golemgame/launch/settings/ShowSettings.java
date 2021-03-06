/*******************************************************************************
 * Copyright 2008, 2009, 2010 Sam Bayless.
 * 
 *     This file is part of Golems.
 * 
 *     Golems is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Golems is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with Golems. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.golemgame.launch.settings;

import java.io.IOException;
import java.util.prefs.Preferences;

import com.golemgame.settings.SettingsManager;
import com.golemgame.states.GeneralSettings;
import com.jme.system.GameSettings;
import com.jme.system.PreferencesGameSettings;

public class ShowSettings {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		showSettings();
	}
	public static boolean showSettings()throws InterruptedException, IOException
	{
		return showSettings(null);
			
	}
	public static boolean showSettings(String reason) throws IOException, InterruptedException {
		SettingsManager.loadSettings();

		final GameSettings settings = new PreferencesGameSettings(Preferences.userRoot().node( "Golems.prefs" + "/SystemPrefs" ));

		
		   settings.setFullscreen(GeneralSettings.getInstance().getFullscreen().isValue());//load this from settings manager, not settings file.
			settings.setDepth(GeneralSettings.getInstance().getBitDepth().getValue()); 
			settings.setFramerate(settings.getFrequency());//default framerate.
			settings.setVerticalSync(GeneralSettings.getInstance().getVsync().isValue());
			
			settings.setWidth((int) GeneralSettings.getInstance().getResolution().getValue().getWidth());
			settings.setHeight((int) GeneralSettings.getInstance().getResolution().getValue().getHeight());
		
		
			 if (settings.getWidth()<640 || settings.getHeight() < 480)
			 {	
				 settings.setWidth(640);
				 settings.setHeight(480);
			 }
			 SimpleGameSettingsPanel panel = new SimpleGameSettingsPanel(settings,reason);
			boolean accept = panel.prompt(settings);
			if(accept)
			{
				GeneralSettings.getInstance().getFullscreen().setValue(settings.isFullscreen());
				GeneralSettings.getInstance().getBitDepth().setValue(settings.getDepth());
				GeneralSettings.getInstance().getVsync().setValue(settings.isVerticalSync());
				GeneralSettings.getInstance().getResolution().getValue().setSize(settings.getWidth(),settings.getHeight());
			
				try{
					GeneralSettings.getInstance().getMaxMemory().setValue(Integer.valueOf(panel.getMaxMemory()));
				}catch(NumberFormatException e)
				{
					//do nothing
				}
				SettingsManager.getInstance().save();
			}
			return accept;
	}
}
