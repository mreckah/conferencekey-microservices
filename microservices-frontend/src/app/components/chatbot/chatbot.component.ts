import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatbotService } from '../../services/chatbot.service';
import { ChatMessage, ChatRequest } from '../../models/chat.interface';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit {
  messages: ChatMessage[] = [];
  userInput: string = '';
  conversationId: string = '';
  isLoading: boolean = false;
  isChatOpen: boolean = false;

  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {
    // Add welcome message
    this.messages.push({
      role: 'assistant',
      content: 'Hello! I\'m your Conference Platform assistant. I can help you find information about keynote speakers and conferences. What would you like to know?',
      timestamp: new Date()
    });
  }

  toggleChat(): void {
    this.isChatOpen = !this.isChatOpen;
  }

  sendMessage(): void {
    if (!this.userInput.trim() || this.isLoading) {
      return;
    }

    // Add user message to chat
    const userMessage: ChatMessage = {
      role: 'user',
      content: this.userInput,
      timestamp: new Date()
    };
    this.messages.push(userMessage);

    // Prepare request
    const request: ChatRequest = {
      message: this.userInput,
      conversationId: this.conversationId || undefined
    };

    // Clear input and set loading
    this.userInput = '';
    this.isLoading = true;

    // Send to backend
    this.chatbotService.sendMessage(request).subscribe({
      next: (response) => {
        // Update conversation ID
        this.conversationId = response.conversationId;

        // Add assistant response
        const assistantMessage: ChatMessage = {
          role: 'assistant',
          content: response.message,
          timestamp: new Date(response.timestamp)
        };
        this.messages.push(assistantMessage);
        this.isLoading = false;

        // Scroll to bottom
        setTimeout(() => this.scrollToBottom(), 100);
      },
      error: (error) => {
        console.error('Error sending message:', error);
        const errorMessage: ChatMessage = {
          role: 'assistant',
          content: 'Sorry, I encountered an error. Please try again.',
          timestamp: new Date()
        };
        this.messages.push(errorMessage);
        this.isLoading = false;
      }
    });

    // Scroll to bottom
    setTimeout(() => this.scrollToBottom(), 100);
  }

  clearChat(): void {
    this.messages = [];
    this.conversationId = '';
    this.ngOnInit(); // Add welcome message again
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  private scrollToBottom(): void {
    const chatMessages = document.querySelector('.chat-messages');
    if (chatMessages) {
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  }

  // Suggested questions
  askSuggestedQuestion(question: string): void {
    this.userInput = question;
    this.sendMessage();
  }
}

