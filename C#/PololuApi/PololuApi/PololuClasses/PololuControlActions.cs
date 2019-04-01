using Pololu.SimpleMotorController;
using Pololu.UsbWrapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PololuApi.PololuClasses
{
    public class PololuControlActions
    {
        public void setSpeedLeft(Smc device, short speed)
        {
            try
            {
                  // Find a device and temporarily connect.
                {
                    device.resume();         // Clear as many errors as possible.
                    device.setSpeed(speed);   // Set the speed to full forward (+100%).
                }
            }
            catch (Exception exception)  // Handle exceptions by displaying them to the user.
            {
                Console.WriteLine(exception.Message);
                device.stop();
            }
        }

        public void stop(List<DeviceListItem> deviceList)
        {
            try
            {
                // Find a device and temporarily connect.
                foreach (var dev in deviceList)
                {
                    Smc device = new Smc(dev);
                    device.stop();
                }
            }
            catch (Exception exception)  // Handle exceptions by displaying them to the user.
            {
                Console.WriteLine(exception.Message);
            }
        }

        public void setSpeedRight(Smc device, short speed)
        {
            try
            {
                // Find a device and temporarily connect.
                {
                    device.resume();         // Clear as many errors as possible.
                    device.setSpeed(speed);   // Set the speed to full forward (+100%).
                }
            }
            catch (Exception exception)  // Handle exceptions by displaying them to the user.
            {
                Console.WriteLine(exception.Message);
                device.stop();
            }
        }

        public void resume(List<DeviceListItem> deviceList)
        {
            try
            {
                // Find a device and temporarily connect.
                foreach (var dev in deviceList)
                {
                    Smc device = new Smc(dev);

                    device.setSpeed(50);
                    device.resume();
                }
            }
            catch (Exception  error)  // Handle exceptions by displaying them to the user.
            {
                Console.WriteLine(error.Message);
            }
        }

        public void disconect(List<DeviceListItem> deviceList)
        {
            try
            {
                // Find a device and temporarily connect.
                foreach (var dev in deviceList)
                {
                    Smc device = new Smc(dev);

                    device.disconnect();
                    
                }
            }
            catch (Exception error)  // Handle exceptions by displaying them to the user.
            {
                Console.WriteLine(error.Message);
            }
        }
    }


}
