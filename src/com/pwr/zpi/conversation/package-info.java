/**
 * Package with classes staring threads which listen for new questions and pass them to agent, and get answer from agent
 * and pass it to human. Information is passed between applications in form of JSON object. It's structure must be common
 * between given client program and server. For now it only has string field "message" in both - listening and talking.
 */
package com.pwr.zpi.conversation;