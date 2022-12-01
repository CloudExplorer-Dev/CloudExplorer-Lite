#!/bin/bash
user=USER_NAME
password=USER_PASSWORD
group=GROUP_NAME

groupadd $group
useradd -g $group -d /home/$user -m $user
echo "$user:$password" | chpasswd

grep "$user" /etc/sudoers &>/dev/null
if [ $? -eq 1 ]; then
  echo -e "Sudoers: $user will be added! \n"
  echo "$user        ALL=(ALL)       NOPASSWD: ALL" >>/etc/sudoers
fi