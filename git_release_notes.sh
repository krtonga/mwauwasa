# We need an initial bullet point for our list of commit logs
echo -n "* "
# Get the latest app uploads
curl -H "X-HockeyAppToken: $HOCKEYAPP_TOKEN" \
"https://rink.hockeyapp.net/api/2/apps/YOUR_HOCKEYAPP_APP_ID/app_versions?page=1" | \
# Put every property on a separate line
sed 's/,/\n/g' | \
# Remove all the quotation marks
sed 's/"//g' | \
# Look at only the notes properties
grep notes | \
# Look at the first one, i.e. the latest app upload
head -n 1 | \
# Find the commit information at the bottom of the notes
sed -n 's/.*(commit:\([^)]*\)).*/\1/p' | \
# Let's find all the logs since that commit
xargs -I '{}' git log {}..HEAD --full --no-merges | \
# Add a star to each newline to make the list
sed ':a;N;$!ba;s/\n/\n* /g'
# The end of the revision log must have the latest commit
# This is so later we can do the above again
echo
echo -n "* (commit:"
git rev-parse HEAD | xargs echo -n
echo -n ')'
