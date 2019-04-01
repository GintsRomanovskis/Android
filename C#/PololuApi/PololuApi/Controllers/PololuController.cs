using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Pololu.SimpleMotorController;
using Pololu.UsbWrapper;
using PololuApi.PololuClasses;
using PololuApi.PololuClasses.Connection;

namespace PololuApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PololuController : ControllerBase
    {

        public bool connect;
        public List<DeviceListItem> listOfConnectedDevices;
        public List<DeviceListItem> connectedDevices;
        PololuControlActions controlDevice;
        
        // GET: api/Pololu
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "Connection", "200 ok" };
        }

        // POST: api/Pololu
        [HttpPost]
        public object Post([FromBody] PololuControls controls)
        {
            PololuConnection conn = new PololuConnection();
            connectedDevices = conn.getConnectedDevices();
            controlDevice = new PololuControlActions();
            connect = conn.connect(controls.connection);
            
            if (connect == true)
            {
                try
                {                
                    if (connectedDevices.Count > 0)
                    {
                        listOfConnectedDevices = conn.connectDevice(connectedDevices);

                        if (true)
                        {
                            if (controls.direction == "left" && listOfConnectedDevices[0].serialNumber != null && controls.stopResume.Length <= 0)
                            {
                                try
                                {
                                    Smc deviceLeftSide = new Smc(listOfConnectedDevices[0]);
                                    controlDevice.setSpeedLeft(deviceLeftSide, (short)controls.speed);
                                    return "Speed of left wheels is set to: " + controls.speed;
                                }
                                catch (Exception error)
                                {
                                    return "Left side controler exception " + error;
                                }

                            }
                            if (controls.direction == "right" && listOfConnectedDevices[1].serialNumber != null && controls.stopResume.Length <= 0)
                            {
                                try
                                {
                                    Smc deviceRightSide = new Smc(listOfConnectedDevices[1]);
                                    controlDevice.setSpeedLeft(deviceRightSide, (short)controls.speed);
                                    return "Speed of right wheels is set to: " + controls.speed;
                                }
                                catch (Exception error)
                                {
                                    return "Right side exception " + error;
                                }
                            }
                        }                     
                        
                        //if json send command  off then method stop all conected devices

                        if (controls.stopResume == "stop" && listOfConnectedDevices.Count > 0)
                        {
                            try
                            {
                                controlDevice.stop(listOfConnectedDevices);
                                return "Pololu stop";
                            }
                            catch (Exception error)
                            {
                                return "Error on stop device" + error.Message;
                            }
                        }
                        if ((controls.stopResume != "stop" && listOfConnectedDevices.Count > 0) && controls.stopResume == "resume")
                        {
                            try
                            {
                                controlDevice.resume(listOfConnectedDevices);
                                return "Pololu resume";
                            }
                            catch (Exception error)
                            {
                                return "Error on resuming device" + error.Message;
                            }
                                                                               }
                        return "Found devices: " + listOfConnectedDevices.Count;
                    }
                    else
                    {                     
                        return "There are no connected devices found";
                    }
                }
                catch (Exception)
                {
                    return StatusCode(StatusCodes.Status500InternalServerError);
                }
            }
            else {
                controlDevice.disconect(listOfConnectedDevices);
                return "Device disconnected";
            }
        }

    }
}
