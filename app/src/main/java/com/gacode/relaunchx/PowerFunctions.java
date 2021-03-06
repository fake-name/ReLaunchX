package com.gacode.relaunchx;

import java.io.DataOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class PowerFunctions {

	public static boolean actionLock(Activity act) {
		if (DeviceInfo.isRooted() && DeviceInfo.EINK_NOOK) {
			try {
				Process p = Runtime.getRuntime().exec(
						act.getResources().getString(R.string.shell));
				try {
					// nook only
					DataOutputStream os = new DataOutputStream(
							p.getOutputStream());
					os.writeChars("su\n");
					SystemClock.sleep(100);
					os.writeChars("sendevent /dev/input/event1 1 116 1\n");
					SystemClock.sleep(100);
					os.writeChars("sendevent /dev/input/event1 1 116 0\n");
				} catch (Exception e) {
				} finally {
					p.destroy();
				}
			} catch (Exception e) {
			}
			return true;
		} else {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(act.getBaseContext());
			final String lockPassword = prefs.getString("lockPassword", "");
			if (lockPassword.length() == 0) {
				Toast.makeText(act, act.getResources().getString(R.string.lock_screen_empty_passwd),
						Toast.LENGTH_SHORT).show();
			} else {
				Intent lockScreen = new Intent(LockScreen.ACTION_LOCK_SCREEN);
				act.sendBroadcast(lockScreen);
			}
			return true;
		}
	}

	public static void actionReboot(Activity act) {
		if (DeviceInfo.isRooted()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			// "Reboot confirmation"
			builder.setTitle(act.getResources().getString(
					R.string.jv_advanced_reboot_confirm_title));
			// "Are you sure to reboot your device ? "
			builder.setMessage(act.getResources().getString(
					R.string.jv_advanced_reboot_confirm_text));
			// "YES"
			final Activity fact = act;
			builder.setPositiveButton(
					act.getResources().getString(R.string.jv_advanced_yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							fact.setContentView(R.layout.reboot);
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								public void run() {
									try {
										Process p = Runtime.getRuntime().exec(
												fact.getResources().getString(
														R.string.shell));
										try {
											DataOutputStream os = new DataOutputStream(
													p.getOutputStream());
											os.writeChars("su\n");
											SystemClock.sleep(100);
											os.writeChars("reboot\n");
										} catch (Exception e) {
										} finally {
											p.destroy();
										}
									} catch (Exception e) {
									}
								}
							}, 500);
						}
					});
			// "NO"
			builder.setNegativeButton(
					act.getResources().getString(R.string.jv_advanced_no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			builder.show();
		} else {
			Toast.makeText(
					act,
					act.getResources()
							.getString(R.string.jv_advanced_root_only),
					Toast.LENGTH_LONG).show();
		}
	}

	public static void actionPowerOff(Activity act) {
		if (DeviceInfo.isRooted()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			// "Reboot confirmation"
			builder.setTitle(act.getResources().getString(
					R.string.jv_advanced_poweroff_confirm_title));
			// "Are you sure to reboot your device ? "
			builder.setMessage(act.getResources().getString(
					R.string.jv_advanced_poweroff_confirm_text));
			// "YES"
			final Activity fact = act;
			builder.setPositiveButton(
					act.getResources().getString(R.string.jv_advanced_yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							fact.setContentView(R.layout.poweroff);
							Timer timer = new Timer();
							timer.schedule(new TimerTask() {
								public void run() {
									try {
										Process p = Runtime.getRuntime().exec(
												fact.getResources().getString(
														R.string.shell));
										try {
											DataOutputStream os = new DataOutputStream(
													p.getOutputStream());
											os.writeChars("su\n");
											SystemClock.sleep(100);
											os.writeChars("reboot -p\n");
										} catch (Exception e) {
										} finally {
											p.destroy();
										}
									} catch (Exception e) {
									}
								}
							}, 500);
						}
					});
			// "NO"
			builder.setNegativeButton(
					act.getResources().getString(R.string.jv_advanced_no),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			builder.show();
		} else {
			Toast.makeText(
					act,
					act.getResources()
							.getString(R.string.jv_advanced_root_only),
					Toast.LENGTH_LONG).show();
		}
	}

}
