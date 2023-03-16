SET SESSION innodb_lock_wait_timeout = 7200;

INSERT INTO `bill_rule` (bill_rule.id, bill_rule.name, bill_rule._groups, bill_rule.filters)
VALUES ('03e3e6d10abea0643af6ba989e2891a0', '按云平台', '[
  {
    \"name\": \"云平台\",
    \"field\": \"provider\",
    \"missName\": \"其他\"
  }
]', '[]'),
       ('4879c74fd81c269e43f529b0b430450a', '按云账号', '[
  {
    \"name\": \"云账号\",
    \"field\": \"cloudAccountId\",
    \"missName\": \"其他\"
  }
]', '[]'),
       ('496cbe329272463e80a56e0e325cae9c', '按产品名称', '[
  {
    \"name\": \"云账号\",
    \"field\": \"cloudAccountId\",
    \"missName\": \"其他\"
  },
  {
    \"name\": \"产品名称\",
    \"field\": \"productName\",
    \"missName\": \"其他\"
  }
]', '[]'),
       ('7e3ab43118d3ecdd8b00f90bd125aad9', '按付费类型', '[
  {
    \"name\": \"云账号\",
    \"field\": \"cloudAccountId\",
    \"missName\": \"其他\"
  },
  {
    \"name\": \"产品名称\",
    \"field\": \"productName\",
    \"missName\": \"其他\"
  },
  {
    \"name\": \"计费模式\",
    \"field\": \"billMode\",
    \"missName\": \"其他\"
  }
]', '[]'),
       ('8cde869e0d0c20f95e8953f7bd397765', '按企业项目', '[
  {
    \"name\": \"云账号\",
    \"field\": \"cloudAccountId\",
    \"missName\": \"其他\"
  },
  {
    \"name\": \"企业项目\",
    \"field\": \"projectName\",
    \"missName\": \"其他\"
  },
  {
    \"name\": \"产品名称\",
    \"field\": \"productName\",
    \"missName\": \"其他\"
  }
]', '[]');

SET SESSION innodb_lock_wait_timeout = DEFAULT;
