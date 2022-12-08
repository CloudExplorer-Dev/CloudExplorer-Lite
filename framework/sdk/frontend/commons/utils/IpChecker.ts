import IpAddr from "ipaddr.js";
import _ from "lodash";

const ipv4 =
  /^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;

const cidrV4 =
  /^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\/([1-9]|[1-2]\d|3[0-2])$/;
const cidrV6 =
  /^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*(\/([0-9]|[1-9][0-9]|1[0-1][0-9]|12[0-8]))?$/;

const netmaskList = [
  "0.0.0.0",
  "128.0.0.0",
  "192.0.0.0",
  "224.0.0.0",
  "240.0.0.0",
  "248.0.0.0",
  "252.0.0.0",
  "254.0.0.0",
  "255.0.0.0",
  "255.128.0.0",
  "255.192.0.0",
  "255.224.0.0",
  "255.240.0.0",
  "255.248.0.0",
  "255.252.0.0",
  "255.254.0.0",
  "255.255.0.0",
  "255.255.128.0",
  "255.255.192.0",
  "255.255.224.0",
  "255.255.240.0",
  "255.255.248.0",
  "255.255.252.0",
  "255.255.254.0",
  "255.255.255.0",
  "255.255.255.128",
  "255.255.255.192",
  "255.255.255.224",
  "255.255.255.240",
  "255.255.255.248",
  "255.255.255.252",
  "255.255.255.254",
  "255.255.255.255",
];

const RegExpPatterns = {
  ipv4,
  cidrV4,
  cidrV6,
  netmaskList,
};

function isValid(ipaddr: string): boolean {
  if (_.split(ipaddr, ".").length !== 4) {
    return false;
  }
  return IpAddr.IPv4.isValid(ipaddr);
}

function isValidV6(ipaddr: string): boolean {
  if (_.indexOf(ipaddr, ":") < 0) {
    return false;
  }
  return IpAddr.IPv6.isValid(ipaddr);
}

function isValidMask(mask: string) {
  return _.indexOf(netmaskList, mask) >= 0;
}

const ruleIpIsValid = (rule: any, value: any, callback: any) => {
  if (value == undefined || value === "" || isValid(value)) {
    callback();
  } else {
    callback(new Error("格式不正确"));
  }
};
const ruleIpIsValidV6 = (rule: any, value: any, callback: any) => {
  if (value == undefined || value === "" || isValidV6(value)) {
    callback();
  } else {
    callback(new Error("格式不正确"));
  }
};

const ruleMaskIsValidV6 = (rule: any, value: any, callback: any) => {
  if (isValidMask(value)) {
    callback();
  } else {
    callback(new Error("格式不正确"));
  }
};

const IpChecker = {
  RegExpPatterns,
  isValid,
  isValidV6,
  ruleIpIsValid,
  ruleIpIsValidV6,
  isValidMask,
  ruleMaskIsValidV6,
};

export default IpChecker;
