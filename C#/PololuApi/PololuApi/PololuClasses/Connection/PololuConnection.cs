using Pololu.SimpleMotorController;
using Pololu.UsbWrapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PololuApi.PololuClasses.Connection
{
    public class PololuConnection
    {
        public bool connn;
        public  List<DeviceListItem> devices;
        public List<DeviceListItem> listOfconnectedDevice;

        public static DeviceListItem Device { get; private set; }

        public bool connect(string connection)
        {
            if (connection == "connect")
            {
                return true;
            }
            else
            {
                return false;
            }

        }
        
        public List<DeviceListItem> getConnectedDevices()
        {
            
            try
            {
                try
                {
                    devices = Smc.getConnectedDevices();
                }
                catch (NotImplementedException)
                {
                    // use vendor and product instead
                    
                }
            }
            catch (Exception e)
            {
                throw new Exception("There was an error getting the list of connected devices.", e);
            }            
            return devices;
        }

        public List<DeviceListItem> connectDevice(List<DeviceListItem> devicelist)
        {          
             
                foreach (DeviceListItem dli in devicelist)
                {
                // If you have multiple devices connected and want to select a particular
                // device by serial number, you could simply add a line like this:
                //   if (dli.serialNumber != "39FF-7406-3054-3036-4923-0743"){ continue; }
                try
                {
                    Smc connDevice = new Smc(dli); // Connect to the device.
                    listOfconnectedDevice.Add(dli);
                }
                catch (Exception)
                {

                    throw;
                }
                   
                   return listOfconnectedDevice;             // Return the device.
                }
                throw new Exception("Could not find device.  Make sure it is plugged in to USB " +
                    "and check your Device Manager (Windows) or run lsusb (Linux).");
            }        
    }
}
