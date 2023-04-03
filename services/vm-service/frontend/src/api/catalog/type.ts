import type { CloudAccount } from "@commons/api/cloud_account/type";

export interface Good extends CloudAccount {
  balance?: number | string;
  serverCount?: number;
  diskCount?: number;
  publicCloud?: boolean;
}
