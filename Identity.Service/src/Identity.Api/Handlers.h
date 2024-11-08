#pragma once
#include <drogon/drogon.h>
#include <stdlib.h>

using Callback = std::function<void (const drogon::HttpResponsePtr &)>;

// Auth handlers
void signUpHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void signInHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void usernameCheck(const drogon::HttpRequestPtr &request, Callback &&callback, std::string &&username);

// Contact handlers
void addContactHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void updateContactInteractHandler(const drogon::HttpRequestPtr &request, Callback &&callback);
void getContactsHandler(const drogon::HttpRequestPtr &request,
                        Callback &&callback,
                        std::string &&requestorId,
                        unsigned int &&pageSize,
                        unsigned int &&pageNumber);
void deleteContactHandler(const drogon::HttpRequestPtr &request,
                          Callback &&callback,
                          std::string &&primaryUserId,
                          std::string &&secondaryUserId);

// User data handlers
void findUsersWithContactCheck(const drogon::HttpRequestPtr &request,
                               Callback &&callback,
                               std::string &&requestorId,
                               std::string &&searchedUsername);
void updateUserStatusHandler(const drogon::HttpRequestPtr &request, Callback &&callback);