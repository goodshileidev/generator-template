npm run build;
archive="fe-`date '+%Y%m%d%H%M'`"
echo $archive

scp -P 57716 -r dist/* shilei@test-ctiv4-webrtc.hybridconnect.jp:/data/incoming/$archive

echo "rm -f /data/app/ngs-admin-web/fe ;ln -s /data/incoming/$archive /data/app/ngs-admin-web/fe;sudo chmod 777 -R /data/app/ngs-admin-web/fe" | ssh -p 57716 shilei@test-ctiv4-webrtc.hybridconnect.jp
